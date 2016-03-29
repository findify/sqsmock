package io.findify.sqsmock.actions

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.{ErrorResponse, SendMessageResponse}
import io.findify.sqsmock.model.Message

import scala.collection.mutable

/**
  * Created by shutty on 3/29/16.
  */
class SendMessageWorker(account:Long, queues:mutable.Map[String,mutable.Queue[Message]]) extends Worker {
  def process(fields:Map[String,String]) = {
    val result = for (
      queueUrl <- fields.get("QueueUrl");
      messageBody <- fields.get("MessageBody");
      queue <- queues.get(queueUrl)
    ) yield {
      val msg = Message(messageBody)
      queue.enqueue(msg)
      HttpResponse(StatusCodes.OK, entity = SendMessageResponse(msg).toXML.toString())
    }
    result.getOrElse(HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "fail").toXML.toString()))
  }
}
