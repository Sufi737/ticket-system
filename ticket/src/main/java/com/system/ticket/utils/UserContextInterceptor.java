package com.system.ticket.utils;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class UserContextInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		System.out.println("UserContextInterceptor called");
		HttpHeaders headers = request.getHeaders();
		headers.add(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
		return execution.execute(request, body);
	}

}
