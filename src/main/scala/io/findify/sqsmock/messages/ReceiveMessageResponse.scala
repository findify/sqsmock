package io.findify.sqsmock.messages

import java.util.Base64
import io.findify.sqsmock.model.Message
import scala.util.Random

/**
  * Created by shutty on 3/29/16.
  */
case class ReceiveMessageResponse(receiptHandle:String, msg:Message) extends Response {
  def toXML =
    <ReceiveMessageResponse>
      <ReceiveMessageResult>
        <Message>
          <MessageId>{msg.id}</MessageId>
          <ReceiptHandle>{receiptHandle}</ReceiptHandle>
          <MD5OfBody>{hex(md5(msg.body))}</MD5OfBody>
          <Body>{msg.body}</Body>
          {msg.attributes.map { case (name, value) => <Attribute><Name>{name}</Name><Value>{value}</Value></Attribute> }}
        </Message>
      </ReceiveMessageResult>
      <ResponseMetadata>
        <RequestId>{uuid}</RequestId>
      </ResponseMetadata>
    </ReceiveMessageResponse>
}

object ReceiveMessageResponse {
  def apply(msg:Message) = new ReceiveMessageResponse(
    receiptHandle = Base64.getEncoder.encodeToString(Random.nextString(128).getBytes("UTF-8")),
    msg = msg
  )
}