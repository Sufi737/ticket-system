package com.system.employee.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	public Optional<Employee> getEmployee(Integer id) {
		ScopedSpan empGetSpan = tracer.startScopedSpan("getEmployeeDatabaseCall");
		try {
			Optional<Employee> employee = employeeRepository.findById(id);
			return employee;
		} catch (Exception e) {
			logger.debug("Exception getting employee by id: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			empGetSpan.tag("peer.service", "mysql");
			empGetSpan.annotate("Client received");
			empGetSpan.finish();
		}
		return null;
	}
	
	public Optional<Employee> getEmployeeByCode(String code) {
		ScopedSpan empGetSpan = tracer.startScopedSpan("getEmployeeDatabaseCall");
		try {
			Optional<Employee> employee = employeeRepository.findByCode(code);
			return employee;
		} catch (Exception e) {
			logger.debug("Exception getting employee by code: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			empGetSpan.tag("peer.service", "mysql");
			empGetSpan.annotate("Client received");
			empGetSpan.finish();
		}
		return null;
	}
	
	public Employee createEmployee(Employee employee) {
		ScopedSpan empSaveSpan = tracer.startScopedSpan("saveEmployeeDatabaseCall");
		try {
			Optional<Employee> existingEmp = employeeRepository.findByEmail(employee.getEmail());
			if (existingEmp.isPresent()) {
				return null;
			}
			employee = employeeRepository.save(employee);
			return employee;
		} catch (Exception e) {
			logger.debug("Exception creating employee: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			empSaveSpan.tag("peer.service", "mysql");
			empSaveSpan.annotate("Client received");
			empSaveSpan.finish();
		}
		return null;
	}
	
	public Employee updateEmployee(Employee employee) {
		ScopedSpan empSaveSpan = tracer.startScopedSpan("saveEmployeeDatabaseCall");
		try {
			Optional<Employee> existingEmp = employeeRepository.findById(employee.getId());
			if (existingEmp.isPresent()) {
				employee = employeeRepository.save(employee);
				return employee;
			}
		} catch (Exception e) {
			logger.debug("Exception updating employee: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			empSaveSpan.tag("peer.service", "mysql");
			empSaveSpan.annotate("Client received");
			empSaveSpan.finish();
		}
		return null;
	}
	
	public void deleteEmployee(Integer id) {
		ScopedSpan empDeleteSpan = tracer.startScopedSpan("deleteEmployeeDatabaseCall");
		try {
			employeeRepository.deleteById(id);
		} catch (Exception e) {
			logger.debug("Exception deleting employee: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			empDeleteSpan.tag("peer.service", "mysql");
			empDeleteSpan.annotate("Client received");
			empDeleteSpan.finish();
		}
	}
}
