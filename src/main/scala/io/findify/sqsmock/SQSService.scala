package io.findify.sqsmock

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import io.findify.sqsmock.actions.{CreateQueueWorker, ReceiveMessageWorker, SendMessageWorker}
import io.findify.sqsmock.messages._
import io.findify.sqsmock.model.{Message, Queue}

import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


/**
  * Created by shutty on 3/29/16.
  */
object SQSService {
  implicit val system = ActorSystem.create("sqsmock")
  val queueCache = mutable.Map[String, mutable.Queue[Message]]("http://localhost:8001/876119091332/foo" -> mutable.Queue())
  def run(account:Long) = {
    implicit val mat = ActorMaterializer()
    val http = Http(system)
    val createQueueWorker = new CreateQueueWorker(account, queueCache)
    val sendMessageWorker = new SendMessageWorker(account, queueCache)
    val receiveMessageWorker = new ReceiveMessageWorker(account, queueCache)
    val route2 =
      logRequest("request", Logging.InfoLevel) {
        path("") {
          post {
            formFieldMap { fields =>
              complete {
                fields.get("Action") match {
                  case Some("SendMessage") => sendMessageWorker.process(fields)
                  case Some("ReceiveMessage") => receiveMessageWorker.process(fields)
                  case Some("CreateQueue") => createQueueWorker.process(fields)
                  case _ => HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "queue not found").toXML.toString())
                }
              }
            }
          }
        }
      }

    http.bindAndHandle(route2, "localhost", 8001)
  }

  def main(args: Array[String]) {
    run(123)
    Await.result(system.whenTerminated, Duration.Inf)
  }
}
