package io.findify.sqsmock.model

import org.joda.time.DateTime

import scala.collection.mutable


/**
  * Created by shutty on 3/30/16.
  */
class QueueCache(params:Queue) {
  case class MessageLease(when:DateTime, msg:Message)

  val queue = mutable.Queue[Message]()
  val received = mutable.Map[String,MessageLease]()

  def enqueue(msg:Message) = queue += msg
  def enqueue(msgs:List[Message]) = msgs.foreach(queue += _)
  def dequeue:Option[ReceivedMessage] = {
    val msg = queue.dequeueFirst(_ => true).map(ReceivedMessage(_))
    msg.foreach(m => received += (m.handle -> MessageLease(DateTime.now(), m.message)))
    msg
  }
  def dequeue(count:Int):List[ReceivedMessage] = {
    (0 until count).flatMap(_ => dequeue).toList
  }
  def delete(handle:String) = received.get(handle) match {
    case Some(lease) =>
      received -= handle
      true
    case None => false
  }
}
