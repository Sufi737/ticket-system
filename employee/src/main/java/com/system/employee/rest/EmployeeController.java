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
import com.system.employee.entities.Employee;
import com.system.employee.entities.EmployeeResponseEntity;
import com.system.employee.entities.Role;
import com.system.employee.services.DepartmentService;
import com.system.employee.services.EmployeeService;
import com.system.employee.services.RoleService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getEmployee(@PathVariable Integer id) {
		Optional<Employee> employeeOptional = employeeService.getEmployee(id);
		if (!employeeOptional.isPresent()) {
			return ResponseEntity.ok("Employee with give id not found");
		}
		Employee employee = employeeOptional.get();
		Department department = null;
		if (employee.getRoleid() != null) {
			Optional<Department> departmentOptional = departmentService.getDepartment(employee.getRoleid());
			department = departmentOptional.isPresent() ? departmentOptional.get() : null;
		}
		Employee manager = null;
		if (employee.getManagerEmployeeId() != null) {
			Optional<Employee> managerOptional = employeeService.getEmployee(employee.getManagerEmployeeId());
			manager = managerOptional.isPresent() ? managerOptional.get() : null;
		}
		Role role = null;
		if (employee.getRoleid() != null) {
			Optional<Role> roleOpt = roleService.getRole(employee.getRoleid());
			role = roleOpt.isPresent() ? roleOpt.get() : null;
		}
		EmployeeResponseEntity employeeRest = new EmployeeResponseEntity(
					employee.getId(),
					employee.getFirstname(),
					employee.getLastname(),
					employee.getEmail(),
					employee.getCode(),
					manager,
					department,
					role
				);
		employeeRest.add(
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(EmployeeController.class).getEmployee(id)
			    ).withSelfRel(),
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(EmployeeController.class)
			    			.createEmployee(employee))
			    .withRel("createEmployee"),
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(EmployeeController.class)
			    			.updateEmployee(employee))
			    .withRel("updateEmployee"),
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(EmployeeController.class)
			    			.deleteEmployee(id))
			    .withRel("deleteLicense"));
		return ResponseEntity.ok(employeeRest);
	}

	@PostMapping
	public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
		employee = employeeService.createEmployee(employee);
		if (employee == null) {
			return ResponseEntity.badRequest().body("Employee with given email already exists");
		}
		return ResponseEntity.ok(employee);
	}
	
	@PutMapping
	public ResponseEntity<?> updateEmployee(@RequestBody Employee employee) {
		if (employee.getId() == null) {
			return ResponseEntity.badRequest().body("Please provide employee id");
		}
		Employee updatedEmp = employeeService.updateEmployee(employee);
		if (updatedEmp == null) {
			return ResponseEntity.badRequest().body("Employee with given id not found");
		}
		return ResponseEntity.ok(updatedEmp);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable Integer id) {
		employeeService.deleteEmployee(id);
		return ResponseEntity.ok("Employee deleted successfully");
	}

}
