package com.system.ticket.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.system.ticket.entities.EmployeeResponseEntity;

@FeignClient("employee")
public interface EmployeeFeignClient {

	@RequestMapping(
		method= RequestMethod.GET,
		value="/employee/code/{code}",
		consumes="application/json"
	)
	EmployeeResponseEntity getEmployeeByCode(@PathVariable("code") String code);
}
