# SQS mock library for Java/Scala

Sqsmock is a web service implementing AWS SQS API, which can be used for local testing of your code using SQS
but without hitting real SQS endpoints.

Implemented API methods:
* CreateQueue (supported params: VisibilityTimeout)
* DeleteMessage
* ReceiveMessage (supported params: MaxNumberOfMessages)
* SendMessage
* SendMessageBatch

## Installation

Sqsmock package is available for Scala 2.11 (on Java 8). To install using SBT, add these
 statements to your `build.sbt`:

    resolvers += Resolver.bintrayRepo("findify", "maven")
    libraryDependencies += "io.findify" %% "sqsmock" % "0.2.1" % "test",

On maven, update your `pom.xml` in the following way:

    // add this entry to <repositories/>
    <repository>
      <id>findify</id>
      <url>https://dl.bintray.com/findify/maven/</url>
    </repository>

    // add this entry to <dependencies/>
    <dependency>
        <groupId>io.findify</groupId>
        <artifactId>sqsmock_2.11</artifactId>
        <version>0.2.1</version>
        <type>pom</type>
        <scope>test</scope>
    </dependency>

## Usage
Scala:

    // create and start SQS API mock
    val api = new SQSService(port = 8001, account = 1)
    api.start()

    // AWS SQS client setup
    val client = new AmazonSQSClient()
    client.setEndpoint("http://localhost:8001")

    // use it as usual
    val queue = client.createQueue("hello").getQueueUrl()
    client.sendMessage(queue, "world")

Java:

    // create and start SQS API mock
    SQSService api = new SQSService(8001, 1)
    api.start()

    // AWS SQS client setup
    AmazonSQSClient client = new AmazonSQSClient()
    client.setEndpoint("http://localhost:8001")

    // use it as usual
    String queue = client.createQueue("hello").getQueueUrl()
    client.sendMessage(queue, "world")

## License

The MIT License (MIT)

Copyright (c) 2016 Findify AB

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.