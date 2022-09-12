package com.system.employee.repositories;

import org.springframework.data.repository.CrudRepository;

import com.system.employee.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {

	public Role findRoleByRoleName(String name);
}
