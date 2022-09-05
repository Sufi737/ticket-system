package com.system.employee.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.employee.entities.Employee;
import com.system.employee.repositories.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	public Optional<Employee> getEmployee(Integer id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		return employee;
	}
	
	public Employee createEmployee(Employee employee) {
		Employee existingEmp = employeeRepository.findByEmail(employee.getEmail());
		if (existingEmp != null) {
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
