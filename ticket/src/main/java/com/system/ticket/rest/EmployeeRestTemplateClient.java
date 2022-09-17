package com.system.ticket.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.system.ticket.entities.EmployeeResponseEntity;

@Component
public class EmployeeRestTemplateClient {
	
	@Autowired
	RestTemplate restTemplate;
	
	public EmployeeResponseEntity getEmployeeByCode(String code) {
		ResponseEntity<EmployeeResponseEntity> restExchange = restTemplate.exchange(
				"http://employee/code/{code}", 
				HttpMethod.GET,
				null,
				EmployeeResponseEntity.class,
				code
		);
		return restExchange.getBody();
	}
} 
