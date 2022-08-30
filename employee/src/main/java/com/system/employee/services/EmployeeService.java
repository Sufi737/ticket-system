package com.system.employee.services;

import org.springframework.stereotype.Service;

import com.system.employee.entities.Employee;

@Service
public class EmployeeService {

	public Employee getEmployee(Integer id) {
		Employee employee = new Employee(
				1,
				"Sufyan",
				"Khot",
				"khotsufyan@gmail.com",
				"EMP0001",
				"547304sdfsdf8sdf6s897d6f9sd8f6s9df68s9df86",
				2,
				1,
				1
		);
		return employee;
	}
	
	public Employee createEmployee(Employee employee) {
		return employee;
	}
	
	public Employee updateEmployee(Employee employee) {
		return employee;
	}
	
	public String deleteEmployee(Integer id) {
		return "Employee deleted successfully";
	}
}
