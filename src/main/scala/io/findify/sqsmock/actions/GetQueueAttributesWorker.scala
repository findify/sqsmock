package io.findify.sqsmock.actions

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.{ErrorResponse, GetQueueAttributesResponse, GetQueueUrlResponse}
import io.findify.sqsmock.model.{QueueCache, Queues}

import scala.collection.mutable

/**
  * Worker to respond to GetQueueUrl requests.
  * @since May 14 2017
  *
  * @param account
  * @param queues
  * @param system
  */
class GetQueueAttributesWorker(account:Long, queues:mutable.Map[String,QueueCache], system:ActorSystem) extends Worker {

  val log = Logger(this.getClass, "get_queue_url_worker")
  def process(fields:Map[String,String]) = {
    // TODO Implement completely. See http://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_GetQueueAttributes.html

    log.warn("Not implemented completely. Returning static result.")
    HttpResponse(StatusCodes.OK, entity = GetQueueAttributesResponse(Map.empty).toXML.toString())
  }
}
