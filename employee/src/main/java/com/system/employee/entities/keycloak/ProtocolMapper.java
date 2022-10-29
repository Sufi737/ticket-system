package com.system.employee.entities.keycloak;

import lombok.Data;

@Data
public class ProtocolMapper {
	public String id;
    public String name;
    public String protocol;
    public String protocolMapper;
    public boolean consentRequired;
    public Config config;
}
