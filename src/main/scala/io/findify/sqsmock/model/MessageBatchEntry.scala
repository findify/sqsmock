package io.findify.sqsmock.model

/**
  * Created by shutty on 3/30/16.
  */
case class MessageBatchEntry(id:String, message:Message)

object MessageBatchEntry {
  def apply(fields:Iterable[(Int,String,String)]) = {
    val attrs = fields.map(f => f._2 -> f._3).toMap
    for (
      id <- attrs.get("Id");
      body <- attrs.get("MessageBody")
    ) yield new MessageBatchEntry(id, Message(body))
  }
}