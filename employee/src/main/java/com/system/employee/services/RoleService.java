package com.system.employee.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.employee.entities.Role;
import com.system.employee.repositories.RoleRepository;

import brave.ScopedSpan;
import brave.Tracer;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	Tracer tracer;
	
	Logger logger = LoggerFactory.getLogger(RoleService.class);
	
	public Optional<Role> getRole(Integer id) {
		ScopedSpan roleGetSpan = tracer.startScopedSpan("getRoleDatabaseCall");
		try {
			Optional<Role> role = roleRepository.findById(id);
			return role;
		} catch (Exception e) {
			logger.debug("Exception getting role by id: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			roleGetSpan.tag("peer.service", "mysql");
			roleGetSpan.annotate("Client received");
			roleGetSpan.finish();
		}
		return null;
	}
	
	public Role createRole(Role role) {
		ScopedSpan roleCreateSpan = tracer.startScopedSpan("saveRoleDatabaseCall");
		try {
			Role existingRole = roleRepository.findRoleByRoleName(role.getRoleName());
			if (existingRole != null) {
				return null;
			}
			role = roleRepository.save(role);
			return role;
		} catch (Exception e) {
			logger.debug("Exception creating role: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			roleCreateSpan.tag("peer.service", "mysql");
			roleCreateSpan.annotate("Client received");
			roleCreateSpan.finish();
		}
		return null;
	}
	
	public Role updateRole(Role role) {
		ScopedSpan roleSaveSpan = tracer.startScopedSpan("saveRoleDatabaseCall");
		try {
			Optional<Role> existingRole = roleRepository.findById(role.getId());
			if (existingRole.isPresent()) {
				role = roleRepository.save(role);
				return role;
			}
		} catch (Exception e) {
			logger.debug("Exception updating role: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			roleSaveSpan.tag("peer.service", "mysql");
			roleSaveSpan.annotate("Client received");
			roleSaveSpan.finish();
		}
		return null;
		
	}
	
	public void deleteRole(Integer id) {
		ScopedSpan deleteRoleSpan = tracer.startScopedSpan("deleteRoleDatabaseCall");
		try {
			roleRepository.deleteById(id);
		} catch (Exception e) {
			logger.debug("Exception deleting role: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			deleteRoleSpan.tag("peer.service", "mysql");
			deleteRoleSpan.annotate("Client received");
			deleteRoleSpan.finish();
		}
	}
}
