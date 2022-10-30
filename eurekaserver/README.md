## Eureka service
This service provides service discovery to other services. In a distributed system, finding the machine to hosts the service via its IP address is called service discovery.
There are 2 advantages in using service discovery:
1. <b>Horizontal scaling is possible:</b> Developers don't need to scale vertically when there is more load. Just add more instances of services.
2. <b>Resiliency is improved:</b> When one instance of a service fails consumer services can use other running instances.

Traditional load balancer + DNS does not work in cloud for below reasons:
1. Load balancer is the single point of failure in entire application
2. Load balancer is static. Adding new service instance entries need to be done manually.

### Service Discovery architecture

We have a service discovery agent cluster which stores data regarding the active instances of services and their locations. Client services call service discovery agent to locate a service and then calls it.

We make use of client-side load balancing where client makes use of a load balancing algorithm. It first calls service discovery agent, takes all running instances location data and stores it in its local cache. Then by making use of a load balancing algorithm such as round robin it calls the services required.

If in a call a service is unhealthy then the local cache is invalidated and a call is again made to service discovery agent. Client services also periodically call service discovery agent to keep their local cache up-to-date.

### Mapping to Spring tech
When a new service instance starts up, it registers itself to the Eureka service. This registration provides Eureka the location and port.

When a client service calls another service, it calls the Spring Cloud Load Balancer, which is a cache stored from Eureka service instances and provide load balancing. Periodically Spring Cloud load balancer calls Eureka service to update its local cache.

### Setting up Eureka server
We have created a separate service for Eureka which is a Spring Boot project. In Spring Initialzr, we add dependencies of Actuator, Config server and Eureka server

In pom.xml exclude Ribbon (refer this service pom.xml file on how to do that)

Add `bootstrap.yml` file in eureka server to use cloud config server
```
spring:
    application:
      name: eurekaserver 
    cloud:
      config: 
        uri: http://configserver:8071
        fail-fast: true
        retry:
          initial-interval: 1500
          multiplier: 1.5
          max-attempts: 100
          max-interval: 1000
      loadbalancer:
        ribbon:
          enabled: false
```

Add `eurekaserver.properties` file in cloud config `src/main/resources/config`. In this file add below properties:
```
server.port=8070
spring.application.name=eurekaserver
eureka.client.serviceUrl.defaultZone=http://localhost:8070/eureka
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```
`registerWithEureka` is false because we don't want to register eureka server in Eureka registry
`fetchRegistry` as false will avoid eureka server to not store service instances data in its local cache
`waitTimeInMsWhenSyncEmpty` is set to 5ms to wait till server accepts requests

### Registering a service in Eureka

For registering a service with Eureka we can add dependency of eureka client in pom.xml
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>
        spring-cloud-starter-netflix-eureka-client
    </artifactId>
</dependency>
```

We need to make sure we have spring.application.name property set in `bootstrap.yml` file

Every service has 2 components: application ID which is the application name and instance ID which is randomly generated

To register the service, we need to add the following config properties
```
eureka.instance.preferIpAddress=true
eureka.client.registerWithEureka=true
eureka.client.fetchRegistry=true
eureka.client.serviceUrl.defaultZone=http://localhost:8070/eureka/
```

In this configuration we provide the Eureka server URL. These properties can be added in config server file or vault

`preferIpAddress` to true will make service register itself based on ip address rather than hostname. This is better in container based environments such as Docker where hostnames are randomly assigned
Next two properties by default are set to true
`defaultZone` may contain comma separated list of URLs when having multiple Eureka servers

Eureka has a REST endpoint http://localhost:8070/eureka/apps/<app-name>
This by default returns XML payload but can be made to return JSON by adding the accepts header
  
Eureka dashboard can be accessed at http://localhost:<port>
Here we can lookup a service and its status
  
### Calling another service from a service
  
There are three ways to call another service:
1. Discovery client

2. Load balancer aware spring rest template. Need to add rest template bean with @LoadBalanced annotation and then need to create a client class

3. Spring Feign client. This requires @EnableFeignClients on main config class and then need to create an interface
  
In this project Spring Feign client is used to call employee service from ticket service. Refer this interface: https://github.com/Sufi737/ticket-system/blob/master/ticket/src/main/java/com/system/ticket/rest/EmployeeFeignClient.java