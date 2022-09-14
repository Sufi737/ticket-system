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

import com.system.ticket.entities.Ticket;
import com.system.ticket.services.TicketService;

@RestController
@RequestMapping("/ticket")
public class TicketController {

	@Autowired
	private TicketService ticketService;
	
	@GetMapping("/{code}")
	public ResponseEntity<?> getTicket(@PathVariable String code) {
		Optional<Ticket> ticketOptional = ticketService.getTicketByCode(code);
		if (!ticketOptional.isPresent()) {
			return ResponseEntity.ok("Ticket with given id not found");
		}
		Ticket ticket = ticketOptional.get();
		return ResponseEntity.ok(ticket);
	}
	
	@PostMapping
	public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {
		ticket = ticketService.createTicket(ticket);
		return ResponseEntity.ok(ticket);
	}
	
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
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTicket(@PathVariable String ticketCode) {
		ticketService.deleteTicket(ticketCode);
		return ResponseEntity.ok("Ticket deleted successfully");
	}
}