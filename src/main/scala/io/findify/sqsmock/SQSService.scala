package io.findify.sqsmock

import akka.actor.ActorSystem
import akka.event.Logging
import akka.event.slf4j.Logger
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
  def run(account:Long):Unit = {
    val log = Logger(system.getClass, "sqs_client")

    implicit val mat = ActorMaterializer()
    val http = Http(system)
    val backend = new SQSBackend(account, system)
    val route2 =
      logRequest("request", Logging.DebugLevel) {
        pathPrefix(IntNumber) { accountId =>
          path(Segment) { queueName =>
            post {
              formFieldMap { fields =>
                complete {
                  backend.process(fields + ("QueueUrl" -> s"http://localhost:8001/$account/$queueName"))
                }
              }
            }
          }
        } ~ post {
          formFieldMap { fields =>
            complete {
              backend.process(fields)
            }
          }
        }
      }
    Await.result(http.bindAndHandle(route2, "localhost", 8001), Duration.Inf)
  }

  def main(args: Array[String]) {
    run(123)
    Await.result(system.whenTerminated, Duration.Inf)
  }
}
