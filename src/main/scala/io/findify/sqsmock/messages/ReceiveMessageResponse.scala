package io.findify.sqsmock.messages

import java.util.Base64

import io.findify.sqsmock.model.{Message, ReceivedMessage}

import scala.util.Random

/**
  * Created by shutty on 3/29/16.
  */
case class ReceiveMessageResponse(msgs:List[ReceivedMessage]) extends Response {
  def toXML =
    <ReceiveMessageResponse>
      <ReceiveMessageResult>
        {
        msgs.map{msg =>
          <Message>
            <MessageId>{msg.message.id}</MessageId>
            <ReceiptHandle>{msg.handle}</ReceiptHandle>
            <MD5OfBody>{hex(md5(msg.message.body))}</MD5OfBody>
            <Body>{msg.message.body}</Body>
            {
            msg.message.attributes.map { case (name, value) => <Attribute><Name>{name}</Name><Value>{value}</Value></Attribute> }
            }
          </Message>
        }}
      </ReceiveMessageResult>
      <ResponseMetadata>
        <RequestId>{uuid}</RequestId>
      </ResponseMetadata>
    </ReceiveMessageResponse>
}
