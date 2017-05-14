package io.findify.sqsmock.messages

/**
  * Response to ListQueues request.
  * @see http://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_GetQueueAttributes.html
  * @since May 13 2017
  * @param attributesMap map of attributes names and value to be returned in the response
  */
case class GetQueueAttributesResponse(attributesMap: Map[String, Any]) extends Response {
  def toXML =
    <GetQueueAttributesResponse>
      <GetQueueAttributesResult>
        <Attribute>
          <Name>VisibilityTimeout</Name>
          <Value>30</Value>
        </Attribute>
        <Attribute>
          <Name>DelaySeconds</Name>
          <Value>0</Value>
        </Attribute>
        <Attribute>
          <Name>ReceiveMessageWaitTimeSeconds</Name>
          <Value>2</Value>
        </Attribute>
      </GetQueueAttributesResult>
    </GetQueueAttributesResponse>
}
