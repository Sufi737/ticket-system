package com.system.employee.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.employee.entities.Department;
import com.system.employee.repositories.DepartmentRepository;

import brave.ScopedSpan;
import brave.Tracer;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	Tracer tracer;
	
	Logger logger = LoggerFactory.getLogger(DepartmentService.class);
	
	public Optional<Department> getDepartment(Integer id) {
		ScopedSpan deptGetSpan = tracer.startScopedSpan("getDepartmentDatabaseCall");
		try {
			Optional<Department> department = departmentRepository.findById(id);
			return department;
		} catch (Exception e) {
			logger.debug("Exception getting department by id: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			deptGetSpan.tag("peer.service", "mysql");
			deptGetSpan.annotate("Client received");
			deptGetSpan.finish();
		}
		return null;
	}
	
	public Department createDepartment(Department department) {
		ScopedSpan deptSpan = tracer.startScopedSpan("createDepartmentDatabaseCall");
		try {
			Department existingDepartment = departmentRepository.findByDepartmentName(department.getDepartmentName());
			if (existingDepartment != null) {
				return null;
			}
			department = departmentRepository.save(department);
			return department;
		} catch (Exception e) {
			logger.debug("Exception creating department by id: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			deptSpan.tag("peer.service", "mysql");
			deptSpan.annotate("Client received");
			deptSpan.finish();
		}
		return null;
		
	}
	
	public Department updateDepartment(Department department) {
		ScopedSpan deptSpan = tracer.startScopedSpan("updateDepartmentDatabaseCall");
		try {
			Optional<Department> existingDepartment = departmentRepository.findById(department.getId());
			if (existingDepartment.isPresent()) {
				department = departmentRepository.save(department);
				return department;
			}
		} catch (Exception e) {
			logger.debug("Exception updating department by id: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			deptSpan.tag("peer.service", "mysql");
			deptSpan.annotate("Client received");
			deptSpan.finish();
		}
		return null;
	}
	
	public void deleteDepartment(Integer id) {
		ScopedSpan deptSpan = tracer.startScopedSpan("deleteDepartmentDatabaseCall");
		try {
			departmentRepository.deleteById(id);
		} catch (Exception e) {
			logger.debug("Exception updating department by id: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			deptSpan.tag("peer.service", "mysql");
			deptSpan.annotate("Client received");
			deptSpan.finish();
		}
	}
}
