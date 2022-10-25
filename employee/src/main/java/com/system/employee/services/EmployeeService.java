package com.system.employee.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.employee.entities.Employee;
import com.system.employee.repositories.EmployeeRepository;

import brave.ScopedSpan;
import brave.Tracer;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	Tracer tracer;

	public Optional<Employee> getEmployee(Integer id) {
		ScopedSpan empGetSpan = tracer.startScopedSpan("getEmployeeDatabaseCall");
		try {
			Optional<Employee> employee = employeeRepository.findById(id);
			return employee;
		} finally {
			empGetSpan.tag("peer.service", "mysql");
			empGetSpan.annotate("Client received");
			empGetSpan.finish();
		}
		
	}
	
	public Optional<Employee> getEmployeeByCode(String code) {
		ScopedSpan empGetSpan = tracer.startScopedSpan("getEmployeeDatabaseCall");
		try {
			Optional<Employee> employee = employeeRepository.findByCode(code);
			return employee;
		} finally {
			empGetSpan.tag("peer.service", "mysql");
			empGetSpan.annotate("Client received");
			empGetSpan.finish();
		}
		
	}
	
	public Employee createEmployee(Employee employee) {
		Optional<Employee> existingEmp = employeeRepository.findByEmail(employee.getEmail());
		if (existingEmp.isPresent()) {
			return null;
		}
		employee = employeeRepository.save(employee);
		return employee;
	}
	
	public Employee updateEmployee(Employee employee) {
		Optional<Employee> existingEmp = employeeRepository.findById(employee.getId());
		if (existingEmp.isPresent()) {
			employee = employeeRepository.save(employee);
			return employee;
		}
		return null;
	}
	
	public void deleteEmployee(Integer id) {
		try {
			employeeRepository.deleteById(id);
		} catch (Exception e) {
			//log exception
		}
	}
}
