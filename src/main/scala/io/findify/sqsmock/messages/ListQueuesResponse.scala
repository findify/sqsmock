package io.findify.sqsmock.messages

/**
  * Response to ListQueues request.
  * @see http://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ListQueues.html
  * @since May 13 2017
  * @param queueNames Names of all queues.
  */
case class ListQueuesResponse(queueNames:Seq[String]) extends Response {
  def toXML =
    <ListQueuesResponse>
      <ListQueuesResult>
        {
          queueNames.map { queueName =>
            <QueueUrl>{queueName}</QueueUrl>
          }
        }
      </ListQueuesResult>
      <ResponseMetadata>
        <RequestId>
          {uuid}
        </RequestId>
      </ResponseMetadata>
    </ListQueuesResponse>
}
