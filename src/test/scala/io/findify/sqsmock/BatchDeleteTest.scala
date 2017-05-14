package io.findify.sqsmock

import com.amazonaws.services.sqs.model.{DeleteMessageBatchRequestEntry, ReceiveMessageRequest, SendMessageBatchRequestEntry}
import org.scalatest._

import scala.collection.JavaConversions._

/**
  * Testing [[io.findify.sqsmock.actions.DeleteMessageBatchWorker]].
  */
class BatchDeleteTest extends FlatSpec with Matchers with SQSStartStop {
  import collection.JavaConverters._

  val queue = "http://localhost:8001/123/foo"

  "sqs mock" should "send delete messages in batch" in {
    // setup some messages
    client.createQueue("foo")
    val batch = List(
      new SendMessageBatchRequestEntry("1", "hello"),
      new SendMessageBatchRequestEntry("2", "world")
    )
    val sendResult = client.sendMessageBatch(queue, batch)
    assert(sendResult.getFailed.isEmpty)
    assert(sendResult.getSuccessful.length == 2)

    // can only delete the message after they have been received
    // since the delete request requires a receive handle.
    val request = new ReceiveMessageRequest()
      .withMaxNumberOfMessages(10)
      .withQueueUrl(queue)
    val result = client.receiveMessage(request)
    assert(result.getMessages.size() == 2)

    // delete the messages
    val deleteBatch = result.getMessages.asScala.map { msg =>
      new DeleteMessageBatchRequestEntry(msg.getMessageId, msg.getReceiptHandle)
    }
    val deleteResult = client.deleteMessageBatch(queue, deleteBatch)
    assert(deleteResult.getFailed.isEmpty)
    assert(deleteResult.getSuccessful.length == 2)

    // queue should now be empty
    val receiveResult = client.receiveMessage(queue)
    assert(receiveResult.getMessages.isEmpty)
  }
}
