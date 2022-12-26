package com.system.employee.rest;

import com.system.employee.dto.RoleDTO;
import com.system.employee.entities.Role;
import com.system.employee.exceptions.RecordAlreadyExistsException;
import com.system.employee.exceptions.RecordNotFoundException;
import com.system.employee.services.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

@RestController
@RequestMapping("/role")
@RolesAllowed("ADMIN")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private ModelMapper modelMapper;

	private RoleDTO getRoleDTO(Role role) {
		return this.modelMapper.map(role, RoleDTO.class);
	}
	
	@GetMapping("/{id}")
	public RoleDTO getRole(@PathVariable Integer id) throws Exception{
		Optional<Role> roleOptional = roleService.getRole(id);
		if (!roleOptional.isPresent()) {
			throw new RecordNotFoundException();
		}
		Role role = roleOptional.get();
		RoleDTO roleDTO = this.getRoleDTO(role);
		roleDTO.add(
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
		
		return roleDTO;
	}

	@PostMapping
	public RoleDTO createRole(@RequestBody Role role) throws Exception {
		role = roleService.createRole(role);
		if (role == null) {
			throw new RecordAlreadyExistsException();
		}
		RoleDTO roleDTO = this.getRoleDTO(role);
		roleDTO.add(
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RoleController.class).getRole(roleDTO.getId())
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
						.deleteRole(roleDTO.getId()))
				.withRel("deleteLicense"));

		return roleDTO;
	}
	
	@PutMapping
	public ResponseEntity<?> updateRole(@RequestBody Role role) {
		//here we need id of the role to update in the request
		if (role.getId() == null) {
			return ResponseEntity.badRequest().body("Please provide role id");
		}
		Role updatedRole = roleService.updateRole(role);
		if (updatedRole == null) {
			throw new RecordNotFoundException();
		}
		return ResponseEntity.ok(updatedRole);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteRole(@PathVariable Integer id) {
		roleService.deleteRole(id);
		return ResponseEntity.ok("Role deleted successfully");
	}
}
