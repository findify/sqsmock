package io.findify.sqsmock.model

import java.util.Base64

import scala.util.Random

/**
  * Created by shutty on 3/30/16.
  */
case class ReceivedMessage(handle:String, message:Message)

object ReceivedMessage {
  def apply(msg:Message) = new ReceivedMessage(
    handle = Base64.getEncoder.encodeToString(Random.nextString(128).getBytes("UTF-8")),
    message = msg
  )
}
