package io.findify.sqsmock.messages

/**
  * Created by shutty on 3/30/16.
  */
object DeleteMessageResponse extends Response {
  def toXML =
    <DeleteMessageResponse>
      <ResponseMetadata>
        <RequestId>
          {uuid}
        </RequestId>
      </ResponseMetadata>
    </DeleteMessageResponse>
}
