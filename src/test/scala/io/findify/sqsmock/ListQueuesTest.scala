package io.findify.sqsmock

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConversions._

/**
  * Testing [[io.findify.sqsmock.actions.ListQueuesWorker]]
  */
class ListQueuesTest extends FlatSpec with Matchers with SQSStartStop {

  import collection.JavaConverters._

  override def beforeAll: Unit = {
    super.beforeAll
    // create some queues
    client.createQueue("foo").getQueueUrl should be ("http://localhost:8001/123/foo")
    client.createQueue("bar").getQueueUrl should be ("http://localhost:8001/123/bar")
    client.createQueue("baz").getQueueUrl should be ("http://localhost:8001/123/baz")
  }

  "sqs mock" should "list all queus" in {
    val response = client.listQueues()
    val queueUrls = response.getQueueUrls.asScala.toList
    queueUrls should contain theSameElementsAs(List(
      "http://localhost:8001/123/foo",
      "http://localhost:8001/123/bar",
      "http://localhost:8001/123/baz"
    ))
  }

  it should "list some queues" in {
    val response1 = client.listQueues("ba")
    response1.getQueueUrls.asScala.toList should contain theSameElementsAs(List(
      "http://localhost:8001/123/bar",
      "http://localhost:8001/123/baz"
    ))

    val response2 = client.listQueues("fo")
    response2.getQueueUrls.asScala.toList should contain theSameElementsAs(List(
      "http://localhost:8001/123/foo"
    ))
  }
}
