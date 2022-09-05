package com.system.employee.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.employee.entities.Department;
import com.system.employee.entities.Role;
import com.system.employee.repositories.DepartmentRepository;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	
	public Optional<Department> getDepartment(Integer id) {
		Optional<Department> department = departmentRepository.findById(id);
		return department;
	}
	
	public Department createDepartment(Department department) {
		Department existingDepartment = departmentRepository.findByDepartmentName(department.getDepartmentName());
		if (existingDepartment != null) {
			return null;
		}
		department = departmentRepository.save(department);
		return department;
	}
	
	public Department updateDepartment(Department department) {
		Optional<Department> existingDepartment = departmentRepository.findById(department.getId());
		if (existingDepartment.isPresent()) {
			department = departmentRepository.save(department);
			return department;
		}
		return null;
	}
	
	public void deleteDepartment(Integer id) {
		try {
			departmentRepository.deleteById(id);
		} catch (Exception e) {
			//log exception
		}
	}
}
