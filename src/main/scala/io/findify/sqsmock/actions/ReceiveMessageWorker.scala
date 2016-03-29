package io.findify.sqsmock.actions

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.{ErrorResponse, ReceiveMessageResponse}
import io.findify.sqsmock.model.Message

import scala.collection.mutable

/**
  * Created by shutty on 3/29/16.
  */
class ReceiveMessageWorker(account:Long, queues:mutable.Map[String,mutable.Queue[Message]]) extends Worker {
  def process(fields:Map[String,String]) = {
    val result = for (
      queueUrl <- fields.get("QueueUrl");
      queue <- queues.get(queueUrl);
      dequeued <- queue.dequeueFirst(_ => true)
    ) yield {
      HttpResponse(StatusCodes.OK, entity = ReceiveMessageResponse(dequeued).toXML.toString())
    }
    result.getOrElse(HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "queue not found").toXML.toString()))
  }
}
