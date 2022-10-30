## Configuration Service

### Overview
This is a service which plays the role of configuration erver. The goal of a configuration server is to keep the configuration of the microservices separate from the service codebase. This eliminates the need to re-deploy our microservices when the configuration is changed.

### Flow:
When a microservice container is fired up, it reads its startup configuration data by making a call to the configuration server.

The configuration server then provides the configuration to the microservice. We can configure the server to read the configuration from a file system or a remote repository. We can also use external services like Vault to store the configuration data in key-value type data structure.

### Spring Cloud Configuration Server
Spring Cloud Configuration Server is built on top of Spring Boot. We can embed it in an existing Spring Boot application or start a new Spring Boot project with server embedded in it.

We can add following dependency in pom.xml

```
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

To have a microservice connect to a configuration server, we need to create a bootstrap.yml file and add below configuration to it

```
spring:
  application:
    name: config-server
server:
  port: 8071
```

You may refer this file in employee or ticket service in this repository.