package com.system.employee.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.system.employee.entities.KeycloakRole;
import com.system.employee.entities.KeycloakUser;

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
	
	private UserRepresentation getUser(String username, String firstname, String lastname) {
		UserRepresentation user = new UserRepresentation();
		user.setUsername(username);
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setEnabled(true);
		return user;
	}
	
	private KeycloakUser getUserFromUsername(String token, String username) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		HttpEntity<?> request = new HttpEntity<>(headers);
		KeycloakUser[] response = restTemplate.exchange(
				serverUrl+"admin/realms/"+realm+"/users?username="+username, 
				HttpMethod.GET, 
				request,
				KeycloakUser[].class).getBody();
		return response[0];
	}
	
	private KeycloakRole getRoleFromRoleName(String token, String name) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		HttpEntity<?> request = new HttpEntity<>(headers);
		return restTemplate.exchange(
				serverUrl+"admin/realms/"+realm+"/roles/"+name, 
				HttpMethod.GET, 
				request,
				KeycloakRole.class).getBody();
	}
	
	private void assignRoleToUser(String token, String userId, String roleId, String roleName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		List<Map<String, String>> requestBody = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		map.put("id", roleId);
		map.put("name", roleName);
		requestBody.add(map);
		HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
		ResponseEntity<Void> response = restTemplate.exchange(
				serverUrl+"admin/realms/"+realm+"/users/"+userId+"/role-mappings/realm", 
				HttpMethod.POST, 
				request, 
				Void.class);
		logger.debug("Role assignment response status: "+response.getStatusCodeValue());
		logger.debug("Role assignment response body: "+response.getBody());
	}

	public void addCredentials(String authToken, String username, String firstname, String lastname, String password) {
		try {
			logger.debug("Sending a token request to: "+serverUrl+"admin/realms/"+realm+"/users");
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", authToken);
			UserRepresentation user = this.getUser(username, firstname, lastname);
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
				logger.debug("Keycloak user created successfully with username: "+username);
				KeycloakUser createdUser = this.getUserFromUsername(authToken, username);
				logger.debug("Keycloak user id fetched successfully: "+createdUser.getId());
				KeycloakRole role = this.getRoleFromRoleName(authToken, "USER");
				logger.debug("Keycloak role fetched successfully: "+role.getId());
				this.assignRoleToUser(authToken, createdUser.getId(), role.getId(), "USER");
			}
		} catch (Exception e) {
			logger.debug("Exception adding credentials for user in Keycloak service: "+e.getMessage());
		}
		
	}
}
