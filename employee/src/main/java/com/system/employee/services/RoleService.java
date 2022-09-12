package com.system.employee.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.employee.entities.Role;
import com.system.employee.repositories.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	public Optional<Role> getRole(Integer id) {
		Optional<Role> role = roleRepository.findById(id);
		return role;
	}
	
	public Role createRole(Role role) {
		Role existingRole = roleRepository.findRoleByRoleName(role.getRoleName());
		if (existingRole != null) {
			return null;
		}
		role = roleRepository.save(role);
		return role;
	}
	
	public Role updateRole(Role role) {
		Optional<Role> existingRole = roleRepository.findById(role.getId());
		if (existingRole.isPresent()) {
			role = roleRepository.save(role);
			return role;
		}
		return null;
	}
	
	public void deleteRole(Integer id) {
		try {
			roleRepository.deleteById(id);
		} catch (Exception e) {
			//log exception
		}
	}
}
