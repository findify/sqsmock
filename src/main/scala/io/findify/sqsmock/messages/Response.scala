package io.findify.sqsmock.messages

import java.security.MessageDigest
import java.util.UUID

/**
  * Created by shutty on 3/29/16.
  */
trait Response {
  def hex(bytes:Array[Byte]) = bytes.map("%02X" format _).mkString
  def md5(value:String) = MessageDigest.getInstance("MD5").digest(value.getBytes)
  def uuid = UUID.randomUUID.toString
}
