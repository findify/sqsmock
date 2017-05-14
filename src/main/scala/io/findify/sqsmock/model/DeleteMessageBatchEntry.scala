package io.findify.sqsmock.model

/**
  * Representing a DeleteMessageBatchRequestEntry.
  *
  * @see http://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteMessageBatchRequestEntry.html
  * @since May 14 2017
  */
case class DeleteMessageBatchEntry(id:String, receiptHandle: String)

object DeleteMessageBatchEntry {

  def apply(fields:Iterable[(Int,String,String)]) = {
    val attrs = fields.map(f => f._2 -> f._3).toMap
    for (
      id <- attrs.get("Id");
      handle <- attrs.get("ReceiptHandle")
    ) yield new DeleteMessageBatchEntry(id, handle)
  }
}