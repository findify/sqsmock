package io.findify.sqsmock.model

/**
  * Created by shutty on 3/29/16.
  */
case class Queue(account:Long, name:String, port:Int, visibilityTimeout:Int) {
  def url = s"http://localhost:$port/$account/$name"
}
