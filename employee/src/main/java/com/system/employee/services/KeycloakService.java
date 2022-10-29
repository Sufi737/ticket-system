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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.system.employee.entities.keycloak.KeycloakClient;
import com.system.employee.entities.keycloak.KeycloakRole;
import com.system.employee.entities.keycloak.KeycloakUser;

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
	
	private KeycloakRole getRoleFromRoleName(String token, String name, String containerId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		HttpEntity<?> request = new HttpEntity<>(headers);
		return restTemplate.exchange(
				serverUrl+"admin/realms/"+realm+"/clients/"+containerId+"/roles/"+name, 
				HttpMethod.GET, 
				request,
				KeycloakRole.class).getBody();
	}
	
	private KeycloakClient getClientByName(String token, String name) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		HttpEntity<?> request = new HttpEntity<>(headers);
		KeycloakClient[] client = restTemplate.exchange(
				serverUrl+"admin/realms/"+realm+"/clients?clientId="+name, 
				HttpMethod.GET, 
				request,
				KeycloakClient[].class).getBody();
		return client[0];
	}
	
	private void assignRoleToUser(String token, String userId, String roleId, String roleName, String containerId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		List<Map<String, Object>> requestBody = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("id", roleId);
		map.put("name", roleName);
		map.put("composite", false);
		map.put("clientRole", true);
		map.put("containerId", containerId);
		requestBody.add(map);
		HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
		ResponseEntity<Void> response = restTemplate.exchange(
				serverUrl+"admin/realms/"+realm+"/users/"+userId+"/role-mappings/clients/"+containerId, 
				HttpMethod.POST, 
				request, 
				Void.class);
	}

	public void addCredentials(String authToken, String username, String firstname, String lastname, String password) {
		try {
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
				KeycloakUser createdUser = this.getUserFromUsername(authToken, username);
				KeycloakClient client = this.getClientByName(authToken, clientId);
				KeycloakRole role = this.getRoleFromRoleName(authToken, "USER", client.getId());
				this.assignRoleToUser(authToken, createdUser.getId(), role.getId(), "USER", client.getId());
			}
		} catch (Exception e) {
			logger.debug("Exception adding credentials for user in Keycloak service: "+e.getMessage());
		}
		
	}
}
