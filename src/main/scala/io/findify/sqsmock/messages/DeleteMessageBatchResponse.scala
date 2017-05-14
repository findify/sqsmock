package io.findify.sqsmock.messages

import io.findify.sqsmock.model.{DeleteMessageBatchEntry, MessageBatchEntry}

/**
  * Response to DeleteMessageBatch request.
  * @see http://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteMessageBatch.html
  * @since May 14 2017
  * @param entries List of deleted messages
  */
case class DeleteMessageBatchResponse(entries:List[DeleteMessageBatchEntry]) extends Response {
  def toXML =
    <DeleteMessageBatchResponse>
      <DeleteMessageBatchResult>
        {
          entries.map { entry =>
            <DeleteMessageBatchResultEntry>
              <Id>{ entry.id }</Id>
            </DeleteMessageBatchResultEntry>
          }
        }
      </DeleteMessageBatchResult>
      <ResponseMetadata>
        <RequestId>{uuid}</RequestId>
      </ResponseMetadata>
    </DeleteMessageBatchResponse>
}
