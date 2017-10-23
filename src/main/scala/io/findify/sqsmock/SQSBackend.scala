package io.findify.sqsmock

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.actions._
import io.findify.sqsmock.messages.ErrorResponse
import io.findify.sqsmock.model.{Message, QueueCache}

import scala.collection.mutable

/**
  * Created by shutty on 3/30/16.
  */
class SQSBackend(account:Long, port:Int, system:ActorSystem) {
  val log = Logger(this.getClass, "sqs_backend")
  val queueCache = mutable.Map[String, QueueCache]()

  val createQueueWorker      = new CreateQueueWorker(account, port, queueCache, system)
  val sendMessageWorker      = new SendMessageWorker(account, queueCache, system)
  val receiveMessageWorker   = new ReceiveMessageWorker(account, queueCache, system)
  val sendMessageBatchWorker = new SendMessageBatchWorker(account, queueCache, system)
  val deleteMessageWorker    = new DeleteMessageWorker(account, queueCache, system)
  val listQueuesWorker       = new ListQueuesWorker(account, queueCache, system)
  val getQueueUrlWorker      = new GetQueueUrlWorker(account, queueCache, system)
  val getQueueAttributesWorker = new GetQueueAttributesWorker(account, queueCache, system)
  val deleteMessageBatchWorker = new DeleteMessageBatchWorker(account, queueCache, system)
  val deleteQueueUrlWorker     = new DeleteQueueUrlWorker(account, queueCache, system)

  def process(fields:Map[String,String]) = {
    log.debug(s"processing request for fields $fields")
    fields.get("Action") match {
      case Some("SendMessage")      => sendMessageWorker.process(fields)
      case Some("SendMessageBatch") => sendMessageBatchWorker.process(fields)
      case Some("ReceiveMessage")   => receiveMessageWorker.process(fields)
      case Some("CreateQueue")      => createQueueWorker.process(fields)
      case Some("DeleteMessage")    => deleteMessageWorker.process(fields)
      case Some("ListQueues")       => listQueuesWorker.process(fields)
      case Some("GetQueueUrl")      => getQueueUrlWorker.process(fields)
      case Some("GetQueueAttributes") => getQueueAttributesWorker.process(fields)
      case Some("DeleteMessageBatch") => deleteMessageBatchWorker.process(fields)
      case Some("DeleteQueue")        => deleteQueueUrlWorker.process(fields)
      case _ => HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "operation not supported").toXML.toString())
    }
  }
}
