package io.findify.sqsmock.messages

import io.findify.sqsmock.model.Message

/**
  * Created by shutty on 3/29/16.
  */
case class SendMessageResponse(msg:Message) extends Response {
  def toXML =
    <SendMessageResponse>
      <SendMessageResult>
        <MD5OfMessageBody>{hex(md5(msg.body))}</MD5OfMessageBody>
        <MD5OfMessageAttributes>{hex(md5(msg.body))}</MD5OfMessageAttributes>
        <MessageId>{msg.id}</MessageId>
      </SendMessageResult>
      <ResponseMetadata>
        <RequestId>{uuid}</RequestId>
      </ResponseMetadata>
    </SendMessageResponse>
}
