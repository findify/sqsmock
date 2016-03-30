package io.findify.sqsmock.messages

import io.findify.sqsmock.model.MessageBatchEntry

/**
  * Created by shutty on 3/30/16.
  */
case class SendMessageBatchResponse(entries:List[MessageBatchEntry]) extends Response {
  def toXML =
    <SendMessageBatchResponse>
      <SendMessageBatchResult>
        {
        entries.map { entry =>
          <SendMessageBatchResultEntry>
            <Id>{entry.id}</Id>
            <MessageId>{entry.message.id}</MessageId>
            <MD5OfMessageBody>{hex(md5(entry.message.body))}</MD5OfMessageBody>
          </SendMessageBatchResultEntry>
        }
        }
      </SendMessageBatchResult>
      <ResponseMetadata>
        <RequestId>{uuid}</RequestId>
      </ResponseMetadata>
    </SendMessageBatchResponse>
}
