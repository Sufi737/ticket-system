package com.system.employee.rest;

import com.system.employee.dto.DepartmentDTO;
import com.system.employee.entities.Department;
import com.system.employee.exceptions.RecordAlreadyExistsException;
import com.system.employee.exceptions.RecordNotFoundException;
import com.system.employee.services.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/department")
//@RolesAllowed("ADMIN")
public class DepartmentController {

	private DepartmentService departmentService;
	private ModelMapper modelMapper;

	DepartmentController(
		DepartmentService departmentService,
		ModelMapper modelMapper
	) {
		this.departmentService = departmentService;
		this.modelMapper = modelMapper;
	}

	private DepartmentDTO getDepartmentDTO(Department department) {
		return this.modelMapper.map(department, DepartmentDTO.class);
	}
	
	@GetMapping("/{id}")
	public DepartmentDTO getDepartment(@PathVariable Integer id) throws Exception{
		Optional<Department> departmentOptional = departmentService.getDepartment(id);
		if (!departmentOptional.isPresent()) {
			throw new RecordNotFoundException();
		}
		Department dept = departmentOptional.get();
		DepartmentDTO deptDTO = this.getDepartmentDTO(dept);
		deptDTO.add(
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(DepartmentController.class).getDepartment(id)
			    ).withSelfRel(),
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(DepartmentController.class)
			    			.createDepartment(dept))
			    .withRel("createDepartment"),
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(DepartmentController.class)
			    			.updateDepartment(dept))
			    .withRel("updateDepartment"),
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(DepartmentController.class)
			    			.deleteDepartment(id))
			    .withRel("deleteDepartment"));
		
		return deptDTO;
	}

	@PostMapping
	public DepartmentDTO createDepartment(@RequestBody Department department) throws Exception {
		department = departmentService.createDepartment(department);
		if (department == null) {
			throw new RecordAlreadyExistsException();
		}
		DepartmentDTO deptDto = this.getDepartmentDTO(department);
		deptDto.add(
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(DepartmentController.class).getDepartment(department.getId())
			).withSelfRel(),
			WebMvcLinkBuilder.linkTo(
					WebMvcLinkBuilder.methodOn(DepartmentController.class)
						.createDepartment(department))
				.withRel("createDepartment"),
			WebMvcLinkBuilder.linkTo(
					WebMvcLinkBuilder.methodOn(DepartmentController.class)
						.updateDepartment(department))
				.withRel("updateDepartment"),
			WebMvcLinkBuilder.linkTo(
					WebMvcLinkBuilder.methodOn(DepartmentController.class)
						.deleteDepartment(department.getId()))
				.withRel("deleteDepartment"));
		return deptDto;
	}
	
	@PutMapping
	public ResponseEntity<?> updateDepartment(@RequestBody Department department) {
		if (department.getId() == null) {
			return ResponseEntity.badRequest().body("Please provide department id");
		}
		Department updatedDept = departmentService.updateDepartment(department);
		if (updatedDept == null) {
			throw new RecordNotFoundException();
		}
		return ResponseEntity.ok(updatedDept);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteDepartment(@PathVariable Integer id) {
		departmentService.deleteDepartment(id);
		return ResponseEntity.ok("Department deleted successfully");
	}
}
