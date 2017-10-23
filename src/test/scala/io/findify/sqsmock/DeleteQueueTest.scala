package io.findify.sqsmock

import org.scalatest.{FlatSpec, Matchers}

/**
  * Testing [[io.findify.sqsmock.actions.ListQueuesWorker]]
  */
class DeleteQueueTest extends FlatSpec with Matchers with SQSStartStop {

  import collection.JavaConverters._

  override def beforeAll: Unit = {
    super.beforeAll
    // create some queues
    client.createQueue("foo").getQueueUrl should be ("http://localhost:8001/123/foo")
    client.createQueue("bar").getQueueUrl should be ("http://localhost:8001/123/bar")
    client.createQueue("baz").getQueueUrl should be ("http://localhost:8001/123/baz")
  }

  "sqs mock" should "delete a queue" in {
    val deleteResponse = client.deleteQueue("http://localhost:8001/123/foo")

    // verify queue has been removed
    val listResponse = client.listQueues()
    val queueUrls = listResponse.getQueueUrls.asScala.toList
    queueUrls should contain theSameElementsAs(List(
      "http://localhost:8001/123/bar",
      "http://localhost:8001/123/baz"
    ))
  }
}
