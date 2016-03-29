package io.findify.sqsmock.model

/**
  * Created by shutty on 3/29/16.
  */
case class Queue(account:Long, name:String) {
  def url = s"http://localhost:8001/$account/$name"
}
