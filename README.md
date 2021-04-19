# SQS mock library for Java/Scala

[![Build Status](https://travis-ci.org/shuttie/sqsmock.svg?branch=master)](https://travis-ci.org/shuttie/sqsmock)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.findify/sqsmock_2.12/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.findify/sqsmock_2.12)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)


Sqsmock is a web service implementing AWS SQS API, which can be used for local testing of your code using SQS
but without hitting real SQS endpoints.

Implemented API methods:
* CreateQueue (supported params: VisibilityTimeout)
* DeleteMessage
* ReceiveMessage (supported params: MaxNumberOfMessages)
* SendMessage
* SendMessageBatch

## Installation

Sqsmock package is available for Scala 2.12 (on Java 8) on Maven Central. To install using SBT, add these
 statements to your `build.sbt`:

    libraryDependencies += "io.findify" %% "sqsmock" % "0.4.0" % "test",

On maven, update your `pom.xml` in the following way:

    // add this entry to <repositories/>

    // add this entry to <dependencies/>
    <dependency>
        <groupId>io.findify</groupId>
        <artifactId>sqsmock_2.12</artifactId>
        <version>0.4.0</version>
        <type>pom</type>
        <scope>test</scope>
    </dependency>

## Usage
Scala:

    // create and start SQS API mock
    val api = new SQSService(port = 8001, account = 1)
    api.start()

    // AWS SQS client setup
    val credentials = new AnonymousAWSCredentials()
    val client = new AmazonSQSClient(credentials)
    client.setEndpoint("http://localhost:8001")

    // use it as usual
    val queue = client.createQueue("hello").getQueueUrl()
    client.sendMessage(queue, "world")

Java:

    // create and start SQS API mock
    SQSService api = new SQSService(8001, 1);
    api.start();

    // AWS SQS client setup
    AWSCredentials credentials = new AnonymousAWSCredentials();
    AmazonSQSClient client = new AmazonSQSClient(credentials);
    client.setEndpoint("http://localhost:8001");

    // use it as usual
    String queue = client.createQueue("hello").getQueueUrl();
    client.sendMessage(queue, "world");

## License

The MIT License (MIT)

Copyright (c) 2016 Findify AB

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.