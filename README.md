# Ticket System

## Overview
Ticket system is a proof of concept backend project which demonstrates the use of microservices architecture using Spring Boot and Spring Cloud. The project aims at providing a system to create and manage support tickets that can be created by employees of an organisation.

## Architecture:
![alt text](https://github.com/Sufi737/ticket-system/blob/master/images/architecture.png?raw=true)


## Available services
Below are the available services 


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