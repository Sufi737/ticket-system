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

import com.system.employee.entities.Role;
import com.system.employee.services.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getRole(@PathVariable Integer id) throws Exception{
		Optional<Role> roleOptional = roleService.getRole(id);
		if (!roleOptional.isPresent()) {
			return ResponseEntity.ok("Role with given id not found");
		}
		Role role = roleOptional.get();
		role.add(
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(RoleController.class).getRole(id)
			    ).withSelfRel(),
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(RoleController.class)
			    			.createRole(role))
			    .withRel("createRole"),
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(RoleController.class)
			    			.updateRole(role))
			    .withRel("updateEmployee"),
			    WebMvcLinkBuilder.linkTo(
			    		WebMvcLinkBuilder.methodOn(RoleController.class)
			    			.deleteRole(id))
			    .withRel("deleteLicense"));
		
		return ResponseEntity.ok(role);
	}

	@PostMapping
	public ResponseEntity<?> createRole(@RequestBody Role role) {
		role = roleService.createRole(role);
		if (role == null) {
			return ResponseEntity.badRequest().body("Role with given name already exists");
		}
		return ResponseEntity.ok(role);
	}
	
	@PutMapping
	public ResponseEntity<?> updateRole(@RequestBody Role role) {
		//here we need id of the role to update in the request
		if (role.getId() == null) {
			return ResponseEntity.badRequest().body("Please provide role id");
		}
		Role updatedRole = roleService.updateRole(role);
		if (updatedRole == null) {
			return ResponseEntity.badRequest().body("Role with given id not found");
		}
		return ResponseEntity.ok(updatedRole);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteRole(@PathVariable Integer id) {
		roleService.deleteRole(id);
		return ResponseEntity.ok("Role deleted successfully");
	}
}
