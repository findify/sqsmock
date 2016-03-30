package io.findify.sqsmock

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.actions.{CreateQueueWorker, ReceiveMessageWorker, SendMessageBatchWorker, SendMessageWorker}
import io.findify.sqsmock.messages.ErrorResponse
import io.findify.sqsmock.model.Message

import scala.collection.mutable

/**
  * Created by shutty on 3/30/16.
  */
class SQSBackend(account:Long, system:ActorSystem) {
  val log = Logger(this.getClass, "sqs_backend")
  val queueCache = mutable.Map[String, mutable.Queue[Message]]()
  val createQueueWorker = new CreateQueueWorker(account, queueCache, system)
  val sendMessageWorker = new SendMessageWorker(account, queueCache, system)
  val receiveMessageWorker = new ReceiveMessageWorker(account, queueCache, system)
  val sendMessageBatchWorker = new SendMessageBatchWorker(account, queueCache, system)

  def process(fields:Map[String,String]) = {
    log.debug(s"processing request for fields $fields")
    fields.get("Action") match {
      case Some("SendMessage") => sendMessageWorker.process(fields)
      case Some("SendMessageBatch") => sendMessageBatchWorker.process(fields)
      case Some("ReceiveMessage") => receiveMessageWorker.process(fields)
      case Some("CreateQueue") => createQueueWorker.process(fields)
      case _ => HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "operation not supported").toXML.toString())
    }
  }
}
