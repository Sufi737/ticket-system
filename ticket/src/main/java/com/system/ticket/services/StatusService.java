package com.system.ticket.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.ticket.entities.Status;
import com.system.ticket.repositories.StatusRepository;


@Service
public class StatusService {

	@Autowired
	private StatusRepository statusRepository;
	
	public Optional<Status> getStatusByCode(String statusCode) {
		return statusRepository.findByStatusCode(statusCode);
	}
	
	public Status createStatus(Status status) {
		Optional<Status> existingStatus = statusRepository.findByStatusCode(status.getStatusCode());
		if (existingStatus.isPresent()) {
			return null;
		}
		status = statusRepository.save(status);
		return status;
	}
	
	public Status updateStatus(Status status) {
		Optional<Status> existingStatus = statusRepository.findByStatusCode(status.getStatusCode());
		if (existingStatus.isPresent()) {
			Status existingStatusObject = existingStatus.get();
			status.setId(existingStatusObject.getId());
			status = statusRepository.save(status);
			return status;
		}
		return null;
	}
	
	public void deleteStatus(String statusCode) {
		try {
			statusRepository.deleteByStatusCode(statusCode);
		} catch (Exception e) {
			//log exception
		}
	}
}
