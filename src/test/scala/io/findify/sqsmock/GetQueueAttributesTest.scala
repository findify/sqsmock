package io.findify.sqsmock

import java.util

import org.scalatest.{FlatSpec, Matchers}

/**
  * Testing [[io.findify.sqsmock.actions.GetQueueAttributesWorker]]
  */
class GetQueueAttributesTest extends FlatSpec with Matchers with SQSStartStop {

  override def beforeAll: Unit = {
    super.beforeAll
    // create some queues
    client.createQueue("foo").getQueueUrl should be ("http://localhost:8001/123/foo")
  }

  "sqs mock" should "return queue attributes" in {
    val response = client.getQueueAttributes("http://localhost:8001/123/foo", new util.ArrayList())
    response.getAttributes.size() should be (3)
  }
}
