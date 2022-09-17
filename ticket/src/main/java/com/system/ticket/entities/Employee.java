package com.system.ticket.entities;

import lombok.Data;

@Data
public class Employee {
	private Integer id;
	private String firstname;
	private String lastname;
	private String email;
	private String code;
    private Integer managerEmployeeId;
    private Integer departmentId;
    private Integer roleid;
}
