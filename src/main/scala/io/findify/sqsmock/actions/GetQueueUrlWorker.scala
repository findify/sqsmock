package io.findify.sqsmock.actions

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.{ErrorResponse, GetQueueUrlResponse, ListQueuesResponse, ReceiveMessageResponse}
import io.findify.sqsmock.model.{QueueCache, Queues}

import scala.collection.mutable

/**
  * Worker to respond to GetQueueUrl requests.
  * @since May 14 2017
  *
  * @param account
  * @param queues
  * @param system
  */
class GetQueueUrlWorker(account:Long, queues:mutable.Map[String,QueueCache], system:ActorSystem) extends Worker {

  val log = Logger(this.getClass, "get_queue_url_worker")
  def process(fields:Map[String,String]) = {
    val result = for (
      queueName <- fields.get("QueueName")
    ) yield {
      // optional field
      val accountId = fields.get("QueueOwnerAWSAccountId")
      val validAccount = accountId.map(_ == s"$account").getOrElse(true)

      if (validAccount) {
        // filter queue on name
        Queues.queueWithName(queues.keys, queueName) match {
          case Some(url) =>
            log.debug(s"Found url $url for queue name $queueName")
            HttpResponse(StatusCodes.OK, entity = GetQueueUrlResponse(Option(url)).toXML.toString())
          case None      =>
            log.debug(s"No url found for queue name $queueName")
            HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "AWS.SimpleQueueService.NonExistentQueue", s"No queue with name '$queueName'").toXML.toString())
        }
      } else {
        // return empty list
        log.debug(s"Given account ${accountId.get} does not match current account $account")
        HttpResponse(StatusCodes.OK, entity = GetQueueUrlResponse(None).toXML.toString())
      }
    }

    result.getOrElse{
      log.warn("cannot send message: possibly, some request parameter is missing")
      HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "oops").toXML.toString())
    }
  }
}
