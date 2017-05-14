package io.findify.sqsmock.actions

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.{DeleteMessageBatchResponse, ErrorResponse, SendMessageBatchResponse}
import io.findify.sqsmock.model.{DeleteMessageBatchEntry, MessageBatchEntry, QueueCache}

import scala.collection.mutable

/**
  * Created by shutty on 3/30/16.
  */
class DeleteMessageBatchWorker(account:Long, queues:mutable.Map[String,QueueCache], system:ActorSystem) extends Worker {

  val log = Logger(this.getClass, "delete_message_batch_worker")

  val fieldFormat = """DeleteMessageBatchRequestEntry\.([0-9]+)\.([0-9A-Za-z\.]+)""".r

  def process(fields:Map[String,String]) = {
    val result = for (
      queueUrl <- fields.get("QueueUrl");
      queue <- queues.get(queueUrl)
    ) yield {
      val deletedMsgs = fields
        .filter(_._1.startsWith("DeleteMessageBatchRequestEntry"))
        .flatMap { case (key,value) => key match {
          case fieldFormat(index, name) => Some((index.toInt, name, value))
          case _ => None
        }}
        .groupBy(_._1)
        .values.toList
        .flatMap(DeleteMessageBatchEntry(_))
        .filter { entry =>
          log.debug(s"deleting message ${entry.id} from queue")
          queue.delete(entry.receiptHandle)
        }

      HttpResponse(StatusCodes.OK, entity = DeleteMessageBatchResponse(deletedMsgs).toXML.toString())
    }
    result.getOrElse {
      log.warn("cannot send message: possibly, some request parameter is missing")
      HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "oops").toXML.toString())
    }
  }
}
