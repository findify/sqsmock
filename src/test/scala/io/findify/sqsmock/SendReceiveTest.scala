package io.findify.sqsmock

import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

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
  it should "dequeue a number of messages according to a `MaxNumberOfMessages` parameter" in {
    client.sendMessage(queue, "message1")
    client.sendMessage(queue, "message2")
    val receiveMessageRequest = new ReceiveMessageRequest(queue).withMaxNumberOfMessages(1)
    val result1 = client.receiveMessage(receiveMessageRequest)
    assert(result1.getMessages.size == 1)
    val result2 = client.receiveMessage(receiveMessageRequest)
    assert(result2.getMessages.size == 1)
    val result3 = client.receiveMessage(receiveMessageRequest)
    assert(result3.getMessages.size == 0)
  }
}
