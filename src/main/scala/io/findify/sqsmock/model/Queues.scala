package io.findify.sqsmock.model

/**
  * Utility functions for queues.
  */
object Queues {

  val queueUrlRegex = """https?://(?:\w+)(?::[0-9]{2,5})?/(?:\w+)/([\w-]+)""".r

  /**
    * @param queueUrls Seq of queue urls.
    * @return A map of queue name and urls.
    */
  def mapQueueUrlsWithName(queueUrls: Traversable[String]): Map[String, String] = {
    queueUrls.map { url =>
      val queueUrlRegex(name) = url
      name -> url
    }.toMap
  }

  /**
    * @param queueUrls All queue urls.
    * @param queueName Wanted queue name.
    * @return Queue urls of queue with given name (if found)
    */
  def queueWithName(queueUrls: Traversable[String], queueName: String): Option[String] = {
    mapQueueUrlsWithName(queueUrls).find {
      case (name, url) => name == queueName
    }.map(_._2)
  }

  /**
    * @param queueUrls All queue urls.
    * @param prefix Wanted queue name prefix.
    * @return All queues with matching prefix.
    */
  def queuesWithPrefix(queueUrls: Traversable[String], prefix: String): Seq[String] = {
    mapQueueUrlsWithName(queueUrls).filter {
      case (name, _) => name.startsWith(prefix)
    }.values.toSeq
  }
}
