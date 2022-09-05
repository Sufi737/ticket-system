package com.system.employee.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.employee.entities.Department;
import com.system.employee.services.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getRole(@PathVariable Integer id) throws Exception{
		Optional<Department> departmentOptional = departmentService.getDepartment(id);
		if (!departmentOptional.isPresent()) {
			return ResponseEntity.ok("Department with given id not found");
		}
		Department dept = departmentOptional.get();
		dept.add(
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(DepartmentController.class).getRole(id)
			    ).withSelfRel(),
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(DepartmentController.class)
			    			.createDepartment(dept))
			    .withRel("createRole"),
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(DepartmentController.class)
			    			.updateDepartment(dept))
			    .withRel("updateEmployee"),
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(DepartmentController.class)
			    			.deleteDepartment(id))
			    .withRel("deleteLicense"));
		
		return ResponseEntity.ok(dept);
	}

	@PostMapping
	public ResponseEntity<?> createDepartment(@RequestBody Department department) {
		department = departmentService.createDepartment(department);
		if (department == null) {
			return ResponseEntity.badRequest().body("Department with given name already exists");
		}
		return ResponseEntity.ok(department);
	}
	
	@PutMapping
	public ResponseEntity<?> updateDepartment(@RequestBody Department department) {
		if (department.getId() == null) {
			return ResponseEntity.badRequest().body("Please provide department id");
		}
		Department updatedDept = departmentService.updateDepartment(department);
		if (updatedDept == null) {
			return ResponseEntity.badRequest().body("Department with given id not found");
		}
		return ResponseEntity.ok(updatedDept);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteDepartment(@PathVariable Integer id) {
		departmentService.deleteDepartment(id);
		return ResponseEntity.ok("Department deleted successfully");
	}
}
