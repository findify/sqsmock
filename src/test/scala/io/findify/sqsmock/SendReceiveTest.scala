package io.findify.sqsmock

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConversions._
/**
  * Created by shutty on 3/30/16.
  */
class SendReceiveTest extends FlatSpec with Matchers with SQSStartStop {
  val queue = "http://localhost:8001/123/foo"
  "sqs mock" should "create queue" in {
    val response = client.createQueue("foo")
    assert(response.getQueueUrl == "http://localhost:8001/123/foo")
  }
  it should "be able to push message to queue" in {
    val result = client.sendMessage(queue, "hello_world")
    assert(result.getMessageId.length > 10)
  }
  it should "receive message from queue" in {
    val result = client.receiveMessage(queue)
    assert(result.getMessages.head.getBody == "hello_world")
  }
  it should "detect empty queue" in {
    val result = client.receiveMessage(queue)
    assert(result.getMessages.isEmpty)
  }
}
