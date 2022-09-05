package com.system.employee.entities;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeResponseEntity extends RepresentationModel<EmployeeResponseEntity>{

	private Integer id;
	private String firstname;
	private String lastname;
	private String email;
	private String employeeCode;
	private Employee manager;
	private Department department;
	private Role role;
}
