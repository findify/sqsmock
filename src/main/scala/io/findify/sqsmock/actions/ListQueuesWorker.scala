package io.findify.sqsmock.actions

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.ListQueuesResponse
import io.findify.sqsmock.model.{QueueCache, Queues}

import scala.collection.mutable

/**
  * Worker to respond to ListQueue requests.
  * @since May 13 2017
  *
  * @param account
  * @param queues
  * @param system
  */
class ListQueuesWorker(account:Long, queues:mutable.Map[String,QueueCache], system:ActorSystem) extends Worker {

  val log = Logger(this.getClass, "list_queues_worker")
  def process(fields:Map[String,String]) = {
    // get queues with prefix or all queues
    val queueNames = fields.get("QueueNamePrefix").map(
      prefix => Queues.queuesWithPrefix(queues.keys, prefix)
    ).getOrElse(queues.keys.toList)

    log.debug("listing queues: {}", queueNames.mkString(","))
    HttpResponse(StatusCodes.OK, entity = ListQueuesResponse(queueNames).toXML.toString)
  }
}
