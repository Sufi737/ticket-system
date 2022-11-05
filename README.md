# Ticket System

## Overview
Ticket system is a proof of concept backend project which demonstrates the use of microservices architecture using Spring Boot and Spring Cloud. The project aims at providing a system to create and manage support tickets that can be created by employees of an organisation.

## Architecture:
![alt text](https://github.com/Sufi737/ticket-system/blob/master/images/architecture.png?raw=true)


## Available services

### Ticket service
Provides management of support tickets that will be created by an employee. The ticket will be assigned to an employee and can be moved from one status to another. To see the list of available endpoints and entities visit here: https://github.com/Sufi737/ticket-system/tree/master/ticket#readme

### Employee service
Provides REST endpoints to manage employees, their roles and departments. Visit here to view the list of REST endpoints: https://github.com/Sufi737/ticket-system/tree/master/employee#readme


### Gateway service
Gateway service is where all the calls to individual microservices are routed through. The client never makes a call directly to the employee or ticket service, but it makes a call to the gateway service which routes the call to the appropriate microservice.

This helps in implementing cross-cutting concerns that we want to implement across all services such as logging and security. Read this for more details: https://github.com/Sufi737/ticket-system/tree/master/gatewayserver

### Keycloak
Keycloak provides user authentication which is required before client makes a call to the gateway service. The client service first has to fetch the authentication token from the Keycloak service. The Keycloak service then returns a token which client has to use in all subsequent calls to the gateway service. 

Keycloak service also secures our employee and ticket service, as the gateway server forwards the authentication token to the employee and ticket service. In this project, the Keycloak is added as a Docker container.

Along with Spring Security, the following dependency is required to connect a service to keycloak:

```
<dependency>
	<groupId>org.keycloak</groupId>
	<artifactId>keycloak-spring-boot-starter</artifactId>
</dependency>
```
And then this configuration
```
keycloak.realm = spmia-realm
keycloak.auth-server-url = http://keycloak:8080/auth/
keycloak.ssl-required = external
keycloak.resource = ticket
keycloak.credentials.secret = sDH5jjreifM4tmStGTlU6SzdSzzZy7V5
keycloak.use-resource-role-mappings = true
keycloak.bearer-only = true
```

### Eureka Service Discovery:
Eureka service is playing the role of providing service discovery here. Read this for more details: https://github.com/Sufi737/ticket-system/tree/master/eurekaserver

### Configuration Server:
Configuration service seperates the configuration from the service codebase. This eliminates the need to restart our microservice instances when the configuration is changed. We only need to change the configuration in our configuration service and restart it.
More details here: https://github.com/Sufi737/ticket-system/tree/master/configserver

### Logging and tracing
ELK (Elasticsearch, Logstash and Kibana) provides aggregation of logs to a single source. In a microservices architecture where there are calls made to multiple services for a single transaction, it becomes difficult to debug a transaction when something goes wrong as it might require logging in to multiple servers to check logs.

Spring Cloud Sleuth solves this problem of tracing by adding below information in all the logs: 
1. Service Name
2. traceId: The traceId is associated for an entire transaction which involves multiple service calls
3. spanId: A spanId is unique for each service call
4. Send to Zipkin flag: A flag that tells whether the log will be sent to Zipkin 

Example:

```
ticket-service_1    | 2022-10-23 12:31:51.802 DEBUG [ticket,b5e54467237dbc81,b5e54467237dbc81] 1 --- [nio-8081-exec-2] com.system.ticket.rest.TicketController  : GET ticket request. Code: INC1
```

With the tracing data added by Spring Cloud Sleuth, the following happens:

1. All individual service instances send log data to Logstash
2. Logstash sends the data to Elasticsearch so that it can be queried later. Elasticsearch indexes and stores the data in a searchable format.
3. Kibana retrieves the data stored in Elasticsearch. In Kibana we can enter trace ID and see the the related log entries

The ELK stack in this project is added as Docker containers (refer docker-compose.yml)

### Zipkin

In Zipkin we can visualize requests by filtering them by service names and span names
We can also search a request using its trace id 
Zipkin provides us a breakdown of a single request as multiple service calls. We can see which service call is taking how much time to check for bottlenecks.