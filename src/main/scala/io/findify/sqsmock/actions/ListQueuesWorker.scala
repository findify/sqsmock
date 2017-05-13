package io.findify.sqsmock.actions

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.ListQueuesResponse
import io.findify.sqsmock.model.QueueCache

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

  val queueUrlRegex = """https?://(?:[a-zA-Z0-9]+)(?::[0-9]{2,5})?/(?:[a-zA-Z0-9]+)/([a-zA-Z0-9]+)""".r

  def queuesWithName: Map[String, String] = {
    queues.keys.map { key =>
      val queueUrlRegex(name) = key
      name -> key
    }.toMap
  }

  val log = Logger(this.getClass, "list_queues_worker")
  def process(fields:Map[String,String]) = {
    val queueNames = fields.get("QueueNamePrefix").map(
      prefix => queuesWithName.filter {
        case (key,_) => key.startsWith(prefix)
      }.values.toList
    ).getOrElse(queues.keys.toList)

    log.debug("listing queues: {}", queueNames.mkString(","))
    HttpResponse(StatusCodes.OK, entity = ListQueuesResponse(queueNames).toXML.toString)
  }
}
