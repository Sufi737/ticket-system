package com.system.employee.entities.keycloak;

import lombok.Data;

@Data
public class Config {
    public String userSessionNote;
    public String userinfoTokenClaim;
    public String idTokenClaim;
    public String accessTokenClaim;
    public String claimName;
    public String jsonTypeLabel;
}
