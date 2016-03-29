package io.findify.sqsmock.actions

import akka.http.scaladsl.model.HttpResponse

/**
  * Created by shutty on 3/29/16.
  */
trait Worker {
  def process(fields:Map[String,String]):HttpResponse
}
