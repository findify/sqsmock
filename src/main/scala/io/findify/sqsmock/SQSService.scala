package io.findify.sqsmock

import akka.actor.ActorSystem
import akka.event.Logging
import akka.event.slf4j.Logger
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.ConfigFactory
import io.findify.sqsmock.actions.{CreateQueueWorker, ReceiveMessageWorker, SendMessageWorker}
import io.findify.sqsmock.messages._
import io.findify.sqsmock.model.{Message, Queue}

import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.collection.JavaConversions._

/**
  * Created by shutty on 3/29/16.
  */
class SQSService(port:Int, account:Int = 1) {
  val config = ConfigFactory.parseMap(Map("akka.http.parsing.illegal-header-warnings" -> "off"))
  implicit val system = ActorSystem.create("sqsmock", config)
  def start():Unit = {
    val log = Logger(system.getClass, "sqs_client")
    implicit val mat = ActorMaterializer()
    val http = Http(system)
    val backend = new SQSBackend(account, port, system)
    val route =
      logRequest("request", Logging.DebugLevel) {
        pathPrefix(IntNumber) { accountId =>
          path(Segment) { queueName =>
            post {
              formFieldMap { fields =>
                complete {
                  backend.process(fields + ("QueueUrl" -> s"http://localhost:${port}/$account/$queueName"))
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
    Await.result(http.bindAndHandle(route, "localhost", 8001), Duration.Inf)
  }

  def shutdown():Unit = Await.result(system.terminate(), Duration.Inf)
  def block():Unit = Await.result(system.whenTerminated, Duration.Inf)
}

object SQSService {
  def main(args: Array[String]) {
    val sqs = new SQSService(8001, 1)
    sqs.start()
    sqs.block
  }
}
