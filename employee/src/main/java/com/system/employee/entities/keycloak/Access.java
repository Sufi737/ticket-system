package com.system.employee.entities.keycloak;

import lombok.Data;

@Data
public class Access {
	public boolean view;
    public boolean configure;
    public boolean manage;
}
