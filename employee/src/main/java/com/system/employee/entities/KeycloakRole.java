package com.system.employee.entities;

import lombok.Data;

@Data
public class KeycloakRole {
	private String id;
	private String name;
	private Boolean composite;
	private Boolean clientRole;
	private String containerId;
	private Object attributes;
}
