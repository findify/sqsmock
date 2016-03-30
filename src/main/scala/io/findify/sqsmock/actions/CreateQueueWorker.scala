package io.findify.sqsmock.actions

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.{CreateQueueResponse, ErrorResponse}
import io.findify.sqsmock.model.{Message, Queue}

import scala.collection.mutable

/**
  * Created by shutty on 3/29/16.
  */
class CreateQueueWorker(account:Long, queues:mutable.Map[String,mutable.Queue[Message]], system:ActorSystem) extends Worker {
  val log = Logger(this.getClass, "create_queue_worker")

  def process(fields:Map[String,String]) = fields.get("QueueName") match {
    case Some(queueName) =>
      val q = Queue(account, queueName)
      log.debug(s"Creating queue $q, url=${q.url}")
      queues += (q.url -> mutable.Queue())
      HttpResponse(StatusCodes.OK, entity = CreateQueueResponse(q).toXML.toString())
    case None =>
      log.warn("missing QueueName parameter, cannot create queue")
      HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "no QueueName parameter").toXML.toString())
  }
}
