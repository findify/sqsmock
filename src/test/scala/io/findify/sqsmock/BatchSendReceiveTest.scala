package io.findify.sqsmock

import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.{ReceiveMessageRequest, SendMessageBatchRequestEntry}
import org.scalatest.{FlatSpec, Matchers, MustMatchers}

import scala.collection.JavaConversions._
/**
  * Created by shutty on 3/30/16.
  */
class BatchSendReceiveTest extends FlatSpec with Matchers {
  SQSService.run(123)
  val client = new AmazonSQSClient()
  client.setEndpoint("http://localhost:8001")
  val queue = "http://localhost:8001/123/foo"
  client.createQueue("foo")
  "sqs mock" should "send messages in batch" in {
    val batch = List(
      new SendMessageBatchRequestEntry("1", "hello"),
      new SendMessageBatchRequestEntry("2", "world")
    )
    val result = client.sendMessageBatch(queue, batch)
    assert(result.getFailed.isEmpty)
    assert(result.getSuccessful.length == 2)
  }
  it should "receive message in batch" in {
    val request = new ReceiveMessageRequest()
    request.setMaxNumberOfMessages(10)
    request.setQueueUrl(queue)
    val result = client.receiveMessage(request)
    assert(result.getMessages.size() == 2)
  }

}
