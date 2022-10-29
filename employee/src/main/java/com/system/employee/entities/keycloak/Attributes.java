package com.system.employee.entities.keycloak;

import lombok.Data;

@Data
public class Attributes { 
    public String idTokenAsDetachedSignature;
    public String samlAssertionSignature;
    public String samlForcePostBinding;
    public String samlMultivaluedRoles;
    public String samlEncrypt;
    public String oauth2DeviceAuthorizationGrantEnabled;
    public String backchannelLogoutRevokeOfflineTokens;
    public String samlServerSignature;
    public String samlServerSignatureKeyinfoExt;
    public String useRefreshTokens;
    public String excludeSessionStateFromAuthResponse;
    public String oidcCibaGrantEnabled;
    public String samlArtifactBinding;
    public String backchannelLogoutSessionRequired;
    public String clientCredentialsUseRefreshToken;
    public String saml_force_name_id_format;
    public String requirePushedAuthorizationRequests;
    public String samlClientSignature;
    public String tlsClientCertificateBoundAccessTokens;
    public String samlAuthnstatement;
    public String displayOnConsentScreen;
    public String samlOnetimeuseCondition;
}
