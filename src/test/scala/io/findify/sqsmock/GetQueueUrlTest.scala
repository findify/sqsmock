package io.findify.sqsmock

import com.amazonaws.services.sqs.model.{GetQueueUrlRequest, QueueDoesNotExistException}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Testing [[io.findify.sqsmock.actions.GetQueueUrlWorker]]
  */
class GetQueueUrlTest extends FlatSpec with Matchers with SQSStartStop {

  override def beforeAll: Unit = {
    super.beforeAll
    // create some queues
    client.createQueue("foo").getQueueUrl should be ("http://localhost:8001/123/foo")
    client.createQueue("bar").getQueueUrl should be ("http://localhost:8001/123/bar")
    client.createQueue("baz").getQueueUrl should be ("http://localhost:8001/123/baz")
  }

  "sqs mock" should "return queue with name" in {
    val response = client.getQueueUrl("foo")
    response.getQueueUrl should be("http://localhost:8001/123/foo")
  }

  it should "return queue if name and account match" in {
    val response = client.getQueueUrl(new GetQueueUrlRequest().withQueueName("foo").withQueueOwnerAWSAccountId("123"))
    response.getQueueUrl should be("http://localhost:8001/123/foo")
  }

  it should "return no url if account does not match" in {
    val response = client.getQueueUrl(new GetQueueUrlRequest().withQueueName("foo").withQueueOwnerAWSAccountId("1"))
    response.getQueueUrl should be("")
  }

  it should "return error if no name matches" in {
    intercept[QueueDoesNotExistException] {
      client.getQueueUrl("non-existant")
    }
  }
}
