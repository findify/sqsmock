name := "sqsmock"

version := "0.3.2"

organization := "io.findify"

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.11.8", "2.12.1")

val akkaVersion = "2.4.16"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.0.3",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.5",
  "joda-time" % "joda-time" % "2.9.4",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "com.amazonaws" % "aws-java-sdk-sqs" % "1.11.32" % "test"
)

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

bintrayOrganization := Some("findify")

parallelExecution in Test := false
