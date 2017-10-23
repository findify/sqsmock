package io.findify.sqsmock.messages

/**
  * Response to ListQueues request.
  * @see http://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_GetQueueUrl.html
  * @since May 13 2017
  * @param queueUrl Optional queue url. If None, not queue url is returned.
  */
case class GetQueueUrlResponse(queueUrl:Option[String]) extends Response {
  def toXML =
    <GetQueueUrlResponse>
      <GetQueueUrlResult>
        {
          queueUrl match {
            case Some(url) => <QueueUrl>{ url }</QueueUrl>
            case None      => <QueueUrl></QueueUrl>
          }
        }
      </GetQueueUrlResult>
      <ResponseMetadata>
        <RequestId>470a6f13-2ed9-4181-ad8a-2fdea142988e</RequestId>
      </ResponseMetadata>
    </GetQueueUrlResponse>
}
