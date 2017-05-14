package io.findify.sqsmock.messages

/**
  * Response to DeleteQueue request.
  * @see http://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteQueue.html
  * @since May 14 2017.
  */
case object DeleteQueueResponse extends Response {
  def toXML =
    <DeleteQueueResponse>
      <ResponseMetadata>
        <RequestId>{uuid}</RequestId>
      </ResponseMetadata>
    </DeleteQueueResponse>
}
