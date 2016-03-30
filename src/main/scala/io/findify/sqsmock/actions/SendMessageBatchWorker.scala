package io.findify.sqsmock.actions

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.{ErrorResponse, SendMessageBatchResponse, SendMessageResponse}
import io.findify.sqsmock.model.{Message, MessageBatchEntry}

import scala.collection.mutable

/**
  * Created by shutty on 3/30/16.
  */
class SendMessageBatchWorker(account:Long, queues:mutable.Map[String,mutable.Queue[Message]], system:ActorSystem) extends Worker {
  val log = Logger(this.getClass, "send_message_batch_worker")
  val fieldFormat = """SendMessageBatchRequestEntry\.([0-9]+)\.([0-9A-Za-z\.]+)""".r
  def process(fields:Map[String,String]) = {
    val result = for (
      queueUrl <- fields.get("QueueUrl");
      queue <- queues.get(queueUrl)
    ) yield {
      val msgs = fields
        .filter(_._1.startsWith("SendMessageBatchRequestEntry"))
        .flatMap { case (key,value) => key match {
          case fieldFormat(index, name) => Some((index.toInt, name, value))
          case _ => None
        }}
        .groupBy(_._1)
        .values.toList
        .flatMap(MessageBatchEntry(_))
      log.debug(s"pushing ${msgs.size} messages to queue")
      msgs.foreach(entry => queue.enqueue(entry.message))
      HttpResponse(StatusCodes.OK, entity = SendMessageBatchResponse(msgs).toXML.toString())
    }
    result.getOrElse {
      log.warn("cannot send message: possibly, some request parameter is missing")
      HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "oops").toXML.toString())
    }
  }
}
