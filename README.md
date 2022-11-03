# Ticket System

## Overview
Ticket system is a proof of concept backend project which demonstrates the use of microservices architecture using Spring Boot and Spring Cloud. The project aims at providing a system to create and manage support tickets that can be created by employees of an organisation.

## Architecture:
![alt text](https://github.com/Sufi737/ticket-system/blob/master/images/architecture.png?raw=true)


## Available services
Below are the available services 


### Ticket service:
Provides management of support tickets that will be created by an employee. The ticket will be assigned to an employee and can be moved from one status to another. To see the list of available endpoints and entities visit here: https://github.com/Sufi737/ticket-system/tree/master/ticket#readme

### Employee service:
Provides REST endpoints to manage employees, their roles and departments. Visit here to view the list of REST endpoints: https://github.com/Sufi737/ticket-system/tree/master/employee#readme