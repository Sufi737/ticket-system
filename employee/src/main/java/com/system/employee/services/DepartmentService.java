package com.system.employee.services;

import brave.ScopedSpan;
import brave.Tracer;
import com.system.employee.entities.Department;
import com.system.employee.repositories.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentService {

	private DepartmentRepository departmentRepository;
	Tracer tracer;
	Logger logger;

	DepartmentService(
		DepartmentRepository departmentRepository,
		Tracer tracer
	) {
		this.departmentRepository = departmentRepository;
		this.tracer = tracer;
		this.logger = LoggerFactory.getLogger(DepartmentService.class);
	}
	
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
		} catch (Exception e) {
			logger.debug("Exception creating department by id: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			deptSpan.tag("peer.service", "mysql");
			deptSpan.annotate("Client received");
			deptSpan.finish();
		}
		return department;
		
	}
	
	public Department updateDepartment(Department department) {
		ScopedSpan deptSpan = tracer.startScopedSpan("updateDepartmentDatabaseCall");
		try {
			Optional<Department> existingDepartment = departmentRepository.findById(department.getId());
			if (existingDepartment.isPresent()) {
				department = departmentRepository.save(department);
				deptSpan.tag("peer.service", "mysql");
				deptSpan.annotate("Client received");
				deptSpan.finish();
				return department;
			}
		} catch (Exception e) {
			logger.debug("Exception updating department by id: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
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
