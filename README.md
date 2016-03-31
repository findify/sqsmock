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

MIT