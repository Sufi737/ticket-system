package com.system.ticket.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.ticket.entities.Status;
import com.system.ticket.services.StatusService;

@RestController
@RequestMapping("/status")
public class StatusController {

	@Autowired
	private StatusService statusService;
	
	@GetMapping("/{code}")
	public ResponseEntity<?> getStatus(@PathVariable String code) {
		Optional<Status> statusOptional = statusService.getStatusByCode(code);
		if (!statusOptional.isPresent()) {
			return ResponseEntity.ok("Status with given code not found");
		}
		Status status = statusOptional.get();
		return ResponseEntity.ok(status);
	}
	
	@PostMapping
	public ResponseEntity<?> createStatus(@RequestBody Status status) {
		status = statusService.createStatus(status);
		return ResponseEntity.ok(status);
	}
	
	@PutMapping
	public ResponseEntity<?> updateStatus(@RequestBody Status status) {
		if (status.getStatusCode() == null) {
			return ResponseEntity.badRequest().body("Please provide status code");
		}
		Status updatedStatus = statusService.updateStatus(status);
		if (updatedStatus == null) {
			return ResponseEntity.badRequest().body("Status with given code not found");
		}
		return ResponseEntity.ok(updatedStatus);
	}
	
	@DeleteMapping("/{statusCode}")
	public ResponseEntity<String> deleteStatus(@PathVariable String statusCode) {
		statusService.deleteStatus(statusCode);
		return ResponseEntity.ok("Status deleted successfully");
	}
}
