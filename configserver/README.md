## Configuration Service

### Overview
This is a service which plays the role of configuration server. The goal of a configuration server is to keep the configuration of the microservices separate from the service codebase. This eliminates the need to re-deploy our microservices when the configuration is changed.

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

### Storing the configuration data

We have 3 ways to store configuration data using Spring Cloud Config:
1. Specifying files at a specific directory on the server
2. On a remote repository
3. Using external service like Spring Vault

In this project the configuration data is stored in files present at src/main/resources/config directory. To configure this a bootstrap.yml file is created at src/main/resources directory with the following configuration

```
spring:
  application:
    name: configserver
  profiles:
    active:
    - native
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/config
server:
  port: 8071
```

For each service and environment combination we can create a properties file. It follows the following format: `<artifactId>-<environment>.properties`
For example, for my employee service with dev profile I will create `employee-dev.properties` file

### Connect a service to the configuration server

To have a microservice connect to a configuration server, we need to create a bootstrap.yml file at src/main/resources directory in the service and add below configuration to it

```
spring:
  application:
    name: employee
  profiles:
    active: dev
  cloud:
    config:
      uri: http://configserver:8071
      fail-fast: true
      retry:
        initial-interval: 1500
        multiplier: 1.5
        max-attempts: 100
        max-interval: 1000
```
Here we provide the URL of the config server. You may refer this file in employee or ticket service in this repository.