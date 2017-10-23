package io.findify.sqsmock

import com.amazonaws.auth.{AWSCredentials, AWSCredentialsProvider, AnonymousAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.sqs.{AmazonSQS, AmazonSQSClient, AmazonSQSClientBuilder}
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

/**
  * Created by shutty on 3/31/16.
  */
trait SQSStartStop extends FlatSpec with BeforeAndAfterAll {
  var sqs: SQSService = _
  var client: AmazonSQS = _

  override def beforeAll = {
    sqs = new SQSService(8001, 123)
    sqs.start()

    client = AmazonSQSClientBuilder.standard()
        .withCredentials(new AWSCredentialsProvider {
          val creds = new AnonymousAWSCredentials()
          override def refresh(): Unit = ()
          override def getCredentials: AWSCredentials = creds
        })
        .withEndpointConfiguration(new EndpointConfiguration("http://localhost:8001", "us-east-1"))
        .build()
  }
  override def afterAll = {
    sqs.stop()
  }
}
