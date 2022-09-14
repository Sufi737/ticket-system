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
	
	public Optional<Status> getTicketByCode(String statusCode) {
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
			status = statusRepository.save(status);
			return status;
		}
		return null;
	}
	
	public void deleteStatus(Integer id) {
		try {
			statusRepository.deleteById(id);
		} catch (Exception e) {
			//log exception
		}
	}
}
