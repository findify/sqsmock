package io.findify.sqsmock.actions

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.{ErrorResponse, SendMessageResponse}
import io.findify.sqsmock.model.Message

import scala.collection.mutable

/**
  * Created by shutty on 3/29/16.
  */
class SendMessageWorker(account:Long, queues:mutable.Map[String,mutable.Queue[Message]], system:ActorSystem) extends Worker {
  val log = Logger(this.getClass, "send_message_worker")
  def process(fields:Map[String,String]) = {
    val result = for (
      queueUrl <- fields.get("QueueUrl");
      messageBody <- fields.get("MessageBody");
      queue <- queues.get(queueUrl)
    ) yield {
      val msg = Message(messageBody)
      log.debug("pushing message {} to queue", msg)
      queue.enqueue(msg)
      HttpResponse(StatusCodes.OK, entity = SendMessageResponse(msg).toXML.toString())
    }
    result.getOrElse {
      log.warn("cannot send message: possibly, some request parameter is missing")
      HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "oops").toXML.toString())
    }
  }
}
