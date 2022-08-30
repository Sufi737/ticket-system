package com.system.employee.entities;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;

@Data
public class Employee extends RepresentationModel<Employee> {

	private int id;
	private String firstname;
	private String lastname;
	private String email;
	private String code;
	private String passwordHash;
    private int managerEmployeeId;
    private int departmentId;
    private int roleid;
    
    //should be removed later as this is not required
	public Employee(Integer id, String firstname, String lastname, String email, String code, String passwordHash,
			Integer managerEmployeeId, Integer departmentId, Integer roleid) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.code = code;
		this.passwordHash = passwordHash;
		this.managerEmployeeId = managerEmployeeId;
		this.departmentId = departmentId;
		this.roleid = roleid;
	}
}
