package com.system.employee.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
public class EmployeeResponseEntity extends RepresentationModel<EmployeeResponseEntity> {

	private Integer id;
	private String firstname;
	private String lastname;
	private String email;
	private String employeeCode;
	private Employee manager;
	private Department department;
	private Role role;
}
