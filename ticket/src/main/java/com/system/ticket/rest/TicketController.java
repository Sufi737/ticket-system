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

import com.system.ticket.entities.EmployeeResponseEntity;
import com.system.ticket.entities.Status;
import com.system.ticket.entities.Ticket;
import com.system.ticket.entities.TicketResponseEntity;
import com.system.ticket.entities.TicketRestRequest;
import com.system.ticket.services.StatusService;
import com.system.ticket.services.TicketService;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/ticket")
public class TicketController {

	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private StatusService statusService;
	
	@Autowired
	private EmployeeFeignClient employeeFeignClient;
	
	@CircuitBreaker(name="ticketService", fallbackMethod="fallbackGetTicket")
	@Bulkhead(name="bulkheadTicketService", fallbackMethod="fallbackGetTicket")
	@GetMapping("/{code}")
	public ResponseEntity<?> getTicket(@PathVariable String code) {
		Optional<Ticket> ticketOptional = ticketService.getTicketByCode(code);
		if (!ticketOptional.isPresent()) {
			return ResponseEntity.ok("Ticket with given id not found");
		}
		Ticket ticket = ticketOptional.get();
		EmployeeResponseEntity createdByEmployee = employeeFeignClient.getEmployeeByCode(ticket.getCreatedBy());
		EmployeeResponseEntity assignedToEmployee = employeeFeignClient.getEmployeeByCode(ticket.getCreatedBy());
		Optional<Status> statusData = statusService.getStatusById(ticket.getStatusId());
		Status status = statusData.isPresent() ? statusData.get() : null;	
		TicketResponseEntity ticketResponse = new TicketResponseEntity(
					ticket.getId(),
					ticket.getTicketCode(),
					ticket.getTitle(),
					ticket.getDescription(),
					createdByEmployee,
					assignedToEmployee,
					ticket.getCreatedAt(),
					ticket.getUpdatedAt(),
					status
				);
		return ResponseEntity.ok(ticketResponse);
	}
	
	@CircuitBreaker(name="ticketService", fallbackMethod="fallbackCreateTicket")
	@Bulkhead(name="bulkheadTicketService", fallbackMethod="fallbackCreateTicket")
	@PostMapping
	public ResponseEntity<?> createTicket(@RequestBody TicketRestRequest ticketRequest) {
		Optional<Status> status = ticketService.getStatusByStatusCode(ticketRequest.getStatusCode());
		if (status.isEmpty()) {
			return ResponseEntity.badRequest().body("Status with given code not found");
		}
		Ticket ticket = new Ticket();
		ticket.setStatusId(status.get().getId());
		ticket.setTitle(ticketRequest.getTitle());
		ticket.setDescription(ticketRequest.getDescription());
		ticket.setCreatedBy(ticketRequest.getCreatedBy());
		ticket.setAssignedTo(ticketRequest.getAssignedTo());
		ticket.setCreatedAt(ticketRequest.getCreatedAt());
		ticket.setUpdatedAt(ticketRequest.getUpdatedAt());
		ticket = ticketService.createTicket(ticket);
		return ResponseEntity.ok(ticket);
	}
	
	@CircuitBreaker(name="ticketService", fallbackMethod="fallbackUpdateTicket")
	@Bulkhead(name="bulkheadTicketService", fallbackMethod="fallbackUpdateTicket")
	@PutMapping
	public ResponseEntity<?> updateTicket(@RequestBody Ticket ticket) {
		if (ticket.getTicketCode() == null) {
			return ResponseEntity.badRequest().body("Please provide ticket ID");
		}
		Ticket updatedticket = ticketService.updateTicket(ticket);
		if (updatedticket == null) {
			return ResponseEntity.badRequest().body("Ticket with given ID not found");
		}
		return ResponseEntity.ok(updatedticket);
	}
	
	@CircuitBreaker(name="ticketService", fallbackMethod="fallbackGetTicket")
	@Bulkhead(name="bulkheadTicketService", fallbackMethod="fallbackGetTicket")
	@DeleteMapping("/{ticketCode}")
	public ResponseEntity<String> deleteTicket(@PathVariable String ticketCode) {
		ticketService.deleteTicket(ticketCode);
		return ResponseEntity.ok("Ticket deleted successfully");
	}
	
	public ResponseEntity<?> fallbackGetTicket(String code, Throwable t) {
		return ResponseEntity.ok("Sorry. This service is not available at the moment.");
	}
	
	public ResponseEntity<?> fallbackCreateTicket(TicketRestRequest ticketRequest, Throwable t) {
		return ResponseEntity.ok("Sorry. This service is not available at the moment.");
	}
	
	public ResponseEntity<?> fallbackUpdateTicket(Ticket ticketRequest, Throwable t) {
		return ResponseEntity.ok("Sorry. This service is not available at the moment.");
	}
}
