## Gateway Server

In microservice architecture we have cross-cutting concerns which we need to span across multiple service calls such as security, logging, tracking. We abstract these concerns out into a single service that acts as a filter and router. This service is called gateway.

All service calls are then routed through a single gateway and come under a single URL. The client service calls via the gateway URL and the gateway then maps the call to the actual service call. The client service will never make call to other service directly, but will call the gateway.

We can have multiple instance of a gateway server running to avoid bottlecks. In this project only one instance is used.

### Spring Cloud Gateway

We create a separate service with Gateway dependency. 

Gateway does routing in 2 ways: automatic and manual

In automatic routing we need to add properties
```
spring.cloud.gateway.discovery.locator.enabled=true
spring.cloud.gateway.discovery.locator.lowerCaseServiceId=true
```
And then gateway will route services by their application names

We can check all available services using `actuator/gateway/routes` endpoint

The URL now to access a service will be: http://<gateway-server-hostname>:<gateway-server-port>/<service-application-name>/endpoint

Example: http://localhost:8072/employee/employee/1 
  
We can also manually route the services using config. To dynamically refresh routes, we have an endpoint `actuator/gateway/refresh` which re-reads the data from the gateway configuration stored in the configuration server.
  
### Predicate and filter factories

Gateway servers allow us to implement cross-cutting concerns such as logging and security. As Aspects are developed for a service, we implement filter factories for the services. 
Predicates allow us to check if therequests fulfill a set of conditions before processing the request
  
There are predicate factories proviced by Spring Cloud that checks if a service call matches certain conditions. These can be added via code or configuration file

Before - matches requests that happen before, Ex: Before 2022–03–12…

After - matches requests that happened later

Between - matches between two time

Header - requires 2 params: header and regex for header value

There are others such as Host, Method, Path, Query, Cookie
  
These are build-in filters which perform some action

AddRequestHeader - Adds an HTTP request header with the name and the value received as parameters.

AddResponseHeader

AddRequestParameter

PrefixPath - Adds a prefix to the HTTP request path 

There are two types of filters: Pre and Post. Pre-filter works before the service is called and post is called later.
  
### Flow:
1. Tracking filter (of gateway server) injects custom header params 

2. UserContextFilter (of service instance) which implements `javax.servlet.Filter` retrieves the param and stores it in `UserContext` object  (this is a custom POJO to store ThreadLocal) 

3. Actual service business logic is executed. Now the code needs to call another service.

4. `UserContextInterceptor` (again of service instance) which implements `ClientHttpRequestInterceptor` makes sure outbound calls have the params set. So before calling another service, we make sure the param is present in the header.

To use `UserContextInterceptor` we need to define a `RestTemplate` bean and add `UserContextInterceptor` to it.