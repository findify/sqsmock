package io.findify.sqsmock

import com.amazonaws.auth.AnonymousAWSCredentials
import com.amazonaws.services.sqs.AmazonSQSClient
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

/**
  * Created by shutty on 3/31/16.
  */
trait SQSStartStop extends FlatSpec with BeforeAndAfterAll {
  var sqs:SQSService = _
  var client:AmazonSQSClient = _
  override def beforeAll = {
    sqs = new SQSService(8001, 123)
    sqs.start()
    client = new AmazonSQSClient(new AnonymousAWSCredentials())
    client.setEndpoint("http://localhost:8001")
  }
  override def afterAll = {
    sqs.shutdown()
  }
}
