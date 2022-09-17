package com.system.ticket.entities;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;

@Data
public class Department extends RepresentationModel<Department>{
	private Integer id;
	private String departmentName;
}
