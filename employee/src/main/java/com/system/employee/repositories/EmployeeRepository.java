package com.system.employee.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.system.employee.entities.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer>{

	public Optional<Employee> findByEmail(String email);
	
	public Optional<Employee> findByCode(String code);
}
