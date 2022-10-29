package com.system.employee.entities;

import java.util.List;
import lombok.Data;

@Data
public class KeycloakUser {
    private String id;
	private String createdTimestamp;
	private String username;
	private Boolean enabled;
	private Boolean totp;
	private String emailVerified;
	private String firstName;
	private String lastName;
	private List<?> disableableCredentialTypes;
	private List<?> requiredActions;
	private KeycloakAccess access;
}
