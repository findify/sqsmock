package io.findify.sqsmock.actions

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.{DeleteQueueResponse, ErrorResponse, GetQueueUrlResponse}
import io.findify.sqsmock.model.{QueueCache, Queues}

import scala.collection.mutable

/**
  * Worker to respond to DeleteQueue requests.
  * @since May 14 2017
  *
  * @param account
  * @param queues
  * @param system
  */
class DeleteQueueUrlWorker(account:Long, queues:mutable.Map[String,QueueCache], system:ActorSystem) extends Worker {

  val log = Logger(this.getClass, "get_queue_url_worker")
  def process(fields:Map[String,String]) = {
    val result = for (
      queueUrl <- fields.get("QueueUrl")
    ) yield {
      log.debug(s"Deleting queue '$queueUrl'")
      queues.remove(queueUrl)
      HttpResponse(StatusCodes.OK, entity = DeleteQueueResponse.toXML.toString)
    }

    result.getOrElse{
      log.warn("cannot send message: possibly, some request parameter is missing")
      HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "oops").toXML.toString())
    }
  }
}
