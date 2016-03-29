name := "sqsmock"

version := "0.1"

organization := "io.findify"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.4.2",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.2",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.5",
  "com.github.nscala-time" %% "nscala-time" % "2.10.0",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test"
)

licenses += ("WTFPL", url("http://www.wtfpl.net/txt/copying/"))

bintrayOrganization := Some("findify")