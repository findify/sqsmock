name := "sqsmock"

version := "0.4.1"

organization := "io.findify"

scalaVersion := "2.12.13"

val akkaVersion = "2.6.14"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.2.4",
  "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
  "joda-time" % "joda-time" % "2.10.10",
  "org.scalatest" %% "scalatest" % "3.0.9" % "test",
  "com.amazonaws" % "aws-java-sdk-sqs" % "1.11.1000" % "test"
)

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

parallelExecution in Test := false

publishMavenStyle := true

publishTo := sonatypePublishToBundle.value

homepage := Some(url("https://github.com/findify/sqsmock"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/findify/sqsmock"),
    "scm:git@github.com:findify/sqsmock.git"
  )
)
developers := List(
  Developer(id = "romangrebennikov", name = "Roman Grebennikov", email = "grv@dfdx.me", url = url("https://dfdx.me/"))
)

scalacOptions += "-target:jvm-1.8"