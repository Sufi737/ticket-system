package com.system.employee.rest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.employee.entities.Employee;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@GetMapping("/{id}")
	public Employee getEmployee(@PathVariable Integer id) {
		//returning static employee for now, will replace with actual data later
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
	
	@PostMapping
	public Employee createEmployee(@RequestBody Employee employee) {
		return employee;
	}
	
	@PutMapping
	public Employee udpateEmployee(@RequestBody Employee employee) {
		return employee;
	}
	
	@DeleteMapping("/{id}")
	public String deleteEmployee(@PathVariable Integer id) {
		return "Employee deleted successfully";
	}
	

}
