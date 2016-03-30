package io.findify.sqsmock

import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.CreateQueueRequest
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConversions._
/**
  * Created by shutty on 3/30/16.
  */
class ReceiveDeleteTest extends FlatSpec with Matchers {
  SQSService.run(123)
  val client = new AmazonSQSClient()
  client.setEndpoint("http://localhost:8001")
  "sqs mock" should "receive & delete message" in {
    val queue = "http://localhost:8001/123/foo"
    val cr = new CreateQueueRequest("foo")
    cr.setAttributes(Map("VisibilityTimeout" -> "1"))
    client.createQueue(cr)
    client.sendMessage(queue, "hello_world")
    val received = client.receiveMessage(queue)
    assert(received.getMessages.nonEmpty)
    assert(client.receiveMessage(queue).getMessages.isEmpty)
    client.deleteMessage(queue, received.getMessages.head.getReceiptHandle)
    Thread.sleep(2000)
    assert(client.receiveMessage(queue).getMessages.isEmpty)
  }
}
