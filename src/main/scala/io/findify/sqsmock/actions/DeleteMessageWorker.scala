package io.findify.sqsmock.actions

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.{DeleteMessageResponse, ErrorResponse, SendMessageResponse}
import io.findify.sqsmock.model.{Message, QueueCache}

import scala.collection.mutable

/**
  * Created by shutty on 3/30/16.
  */
class DeleteMessageWorker(account:Long, queues:mutable.Map[String,QueueCache], system:ActorSystem) extends Worker {
  val log = Logger(this.getClass, "delete_message_worker")
  def process(fields:Map[String,String]) = {
    val result = for (
      queueUrl <- fields.get("QueueUrl");
      handle <- fields.get("ReceiptHandle");
      queue <- queues.get(queueUrl)
    ) yield {
      log.debug("deleting message from queue")
      queue.delete(handle)
      HttpResponse(StatusCodes.OK, entity = DeleteMessageResponse.toXML.toString())
    }
    result.getOrElse {
      log.warn("cannot send message: possibly, some request parameter is missing")
      HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "oops").toXML.toString())
    }
  }
}
