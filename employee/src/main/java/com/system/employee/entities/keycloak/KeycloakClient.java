package com.system.employee.entities.keycloak;

import java.util.ArrayList;

import lombok.Data;

@Data
public class KeycloakClient {
	public String id;
    public String clientId;
    public boolean surrogateAuthRequired;
    public boolean enabled;
    public boolean alwaysDisplayInConsole;
    public String clientAuthenticatorType;
    public ArrayList<String> redirectUris;
    public ArrayList<String> webOrigins;
    public int notBefore;
    public boolean bearerOnly;
    public boolean consentRequired;
    public boolean standardFlowEnabled;
    public boolean implicitFlowEnabled;
    public boolean directAccessGrantsEnabled;
    public boolean serviceAccountsEnabled;
    public boolean authorizationServicesEnabled;
    public boolean publicClient;
    public boolean frontchannelLogout;
    public String protocol;
    public Attributes attributes;
    public AuthenticationFlowBindingOverrides authenticationFlowBindingOverrides;
    public boolean fullScopeAllowed;
    public int nodeReRegistrationTimeout;
    public ArrayList<ProtocolMapper> protocolMappers;
    public ArrayList<String> defaultClientScopes;
    public ArrayList<String> optionalClientScopes;
    public Access access;
}
