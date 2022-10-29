package com.system.employee.entities.keycloak;

import lombok.Data;

@Data
public class KeycloakAccess {
    private Boolean manageGroupMembership;
	private Boolean view;
	private Boolean mapRoles;
	private Boolean impersonate;
	private Boolean manage;
}
