# Sample SpringBoot application using Datastax Astra REST APIs
Very simple application exercising the REST APIs. There is an intent to also test the kafka connector.

## Table of Contents
* [Prerequisites](#Prerequisites)
   * [Installing and Running Kafka](#installing-and-running-kafka)
* [Building and Running locally](#building-and-running-locally)
   * [Building locally](#building-locally)
   * [Running locally](#running-locally)
      * [Testing application apis](#testing-application-apis)
* [References](#references)

## Prerequisites

### **Installing and Running Kafka**
These instructions are for MacOs using HomeBrew

**zookeeper** 

To have launchd start zookeeper now and restart at login:
```
  brew services start zookeeper
```
Or, if you don't want/need a background service you can just run:
```
  zkServer start
```

**kafka**

To have launchd start kafka now and restart at login:
```
  brew services start kafka
```
Or, if you don't want/need a background service you can just run:
```
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties
```

## Building and running locally
This project contains 2 SpringBoot microservices. 
* processor - simulates an event-processor reading messages from a Kafka topic and storing on MongoDb
* service - set of REST APIs to access the Astra data. There is also an simple web ui and OpenAPI/swagger site for integration with API Gateways

We use Maven to build these projects. On each project folder (processor or service) you'll find the pom.xml file.

These microservices use [Spring Properties to externalize configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config) to external services like Kafka and MongoDb. In order to support various environments, [Spring Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-profiles) are used. 

* boostrap.yml and application.yml - are based files loaded by default.
* application-local.yml - used for using the local deployment of Kafka and MongoDb
* application-ocp.yml - used to support OpenShift Container Platform environments

### Building Locally
Normal Maven phases (compile, test, packate, etc) are used and the [SpringBoot Maven Plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/) is configure for additional control.

> You can run ```mvn spring-boot:help``` for information on the plugin goals

### Running Locally
Run locally with the correct profile by executing:
```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

#### Testing application apis
This microservices connects to Kafka and MongoDb and provides the following endpoints:

* /kafka/publish - for testing producing messages of the expected type. It will read all messages in the topic `users`.

Below is a sample curl command to product messages. Alternatively, you can use any other Kafka client.

```
curl -X POST "http://localhost:9000/kafka/publish?name=kobe&age=41"
```

## References
