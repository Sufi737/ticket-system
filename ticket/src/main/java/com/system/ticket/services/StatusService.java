package com.system.ticket.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.ticket.entities.Status;
import com.system.ticket.repositories.StatusRepository;

import brave.ScopedSpan;
import brave.Tracer;


@Service
public class StatusService {

	@Autowired
	private StatusRepository statusRepository;
	
	@Autowired
	Tracer tracer;
	
	Logger logger = LoggerFactory.getLogger(StatusService.class);
	
	public Optional<Status> getStatusById(Integer id) {
		ScopedSpan statusSpan = tracer.startScopedSpan("getStatusDatabaseCall");
		try {
			return statusRepository.findById(id);
		} catch (Exception e) {
			logger.debug("Exception getting status by id: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			statusSpan.tag("peer.service", "mysql");
			statusSpan.annotate("Client received");
			statusSpan.finish();
		}
		return null;
	}
	
	public Optional<Status> getStatusByCode(String statusCode) {
		ScopedSpan statusSpan = tracer.startScopedSpan("getStatusDatabaseCall");
		try {
			return statusRepository.findByStatusCode(statusCode);
		} catch (Exception e) {
			logger.debug("Exception getting status by code: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			statusSpan.tag("peer.service", "mysql");
			statusSpan.annotate("Client received");
			statusSpan.finish();
		}
		return null;
	}
	
	public Status createStatus(Status status) {
		ScopedSpan statusSpan = tracer.startScopedSpan("createStatusDatabaseCall");
		try {
			Optional<Status> existingStatus = statusRepository.findByStatusCode(status.getStatusCode());
			if (existingStatus.isPresent()) {
				return null;
			}
			status = statusRepository.save(status);
			return status;
		} catch (Exception e) {
			logger.debug("Exception creating status: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			statusSpan.tag("peer.service", "mysql");
			statusSpan.annotate("Client received");
			statusSpan.finish();
		}
		return null;
		
	}
	
	public Status updateStatus(Status status) {
		ScopedSpan statusSpan = tracer.startScopedSpan("updateStatusDatabaseCall");
		try {
			Optional<Status> existingStatus = statusRepository.findByStatusCode(status.getStatusCode());
			if (existingStatus.isPresent()) {
				Status existingStatusObject = existingStatus.get();
				status.setId(existingStatusObject.getId());
				status = statusRepository.save(status);
				return status;
			}
		} catch (Exception e) {
			logger.debug("Exception updating status: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			statusSpan.tag("peer.service", "mysql");
			statusSpan.annotate("Client received");
			statusSpan.finish();
		}
		return null;
	}
	
	public void deleteStatus(String statusCode) {
		ScopedSpan statusSpan = tracer.startScopedSpan("deleteStatusDatabaseCall");
		try {
			statusRepository.deleteByStatusCode(statusCode);
		} catch (Exception e) {
			logger.debug("Exception deleting status: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			statusSpan.tag("peer.service", "mysql");
			statusSpan.annotate("Client received");
			statusSpan.finish();
		}
	}
}
