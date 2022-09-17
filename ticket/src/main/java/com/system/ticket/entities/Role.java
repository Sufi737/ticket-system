package com.system.ticket.entities;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;

@Data
public class Role extends RepresentationModel<Role>{
	private Integer id;
	private String roleName;
}
