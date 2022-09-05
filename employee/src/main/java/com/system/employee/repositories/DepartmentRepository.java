package com.system.employee.repositories;

import org.springframework.data.repository.CrudRepository;

import com.system.employee.entities.Department;

public interface DepartmentRepository extends CrudRepository<Department, Integer> {

	public Department findByDepartmentName(String name);
}
