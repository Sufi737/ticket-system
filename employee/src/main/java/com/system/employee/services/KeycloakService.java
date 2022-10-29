package com.system.employee.services;

import java.util.Arrays;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloakService {
	
	@Value("${keycloak.auth-server-url}")
	String serverUrl;
	
	@Value("${keycloak.realm}")
	String realm;
	
	@Value("${keycloak.resource}")
	String clientId;
	
	@Value("${keycloak.credentials.secret}")
	String clientSecret;
	
	@Autowired
	private RestTemplate restTemplate;
	
	Logger logger = LoggerFactory.getLogger(KeycloakService.class);

	public void addCredentials(String authToken, String username, String firstname, String lastname, String password) {
		logger.debug("Sending a token request to: "+serverUrl+"admin/realms/"+realm+"/users");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authToken);
		UserRepresentation user = new UserRepresentation();
		user.setUsername(username);
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setEnabled(true);
		CredentialRepresentation credentials = new CredentialRepresentation();
		credentials.setType("password");
		credentials.setValue(password);
		user.setCredentials(Arrays.asList(credentials));
		HttpEntity<?> request = new HttpEntity<>(user, headers);
		ResponseEntity<?> response = restTemplate.exchange(
				serverUrl+"admin/realms/"+realm+"/users", 
				HttpMethod.POST, 
				request, 
				ResponseEntity.class);
		if (response.getStatusCode() == HttpStatus.CREATED) {
			logger.debug("Keycloak user created successfully with username"+username);
		}
	}
}
