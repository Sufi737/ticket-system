package com.system.employee.repositories;

import org.springframework.data.repository.CrudRepository;

import com.system.employee.entities.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer>{

	public Employee findByEmail(String email);
}
