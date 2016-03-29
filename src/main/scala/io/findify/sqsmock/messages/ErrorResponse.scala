package io.findify.sqsmock.messages

import java.util.UUID

/**
  * Created by shutty on 3/29/16.
  */
case class ErrorResponse(errorType:String, code:String, message:String) {
  def toXML =
    <ErrorResponse>
      <Error>
        <Type>{errorType}</Type>
        <Code>{code}</Code>
        <Message>{message}</Message>
      </Error>
      <RequestId>{UUID.randomUUID.toString}</RequestId>
    </ErrorResponse>
}
