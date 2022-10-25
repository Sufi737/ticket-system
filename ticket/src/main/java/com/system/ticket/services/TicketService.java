package com.system.ticket.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.system.ticket.entities.Status;
import com.system.ticket.entities.Ticket;
import com.system.ticket.repositories.TicketRepository;

import brave.ScopedSpan;
import brave.Tracer;

@Service
public class TicketService {

	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private StatusService statusService;
	
	@Value("${ticket.prefix}")
	private String ticketCodePrefix;
	
	@Autowired
	Tracer tracer;
	
	public String createTicketCode(Integer ticketId) {
		return ticketCodePrefix+ticketId;
	}
	
	public Optional<Ticket> getTicketByCode(String ticketCode) {
		ScopedSpan ticketGetSpan = tracer.startScopedSpan("getTicketDatabaseCall");
		try {
			Optional<Ticket> ticketOpt = ticketRepository.findByTicketCode(ticketCode);
			if (ticketOpt.isPresent()) {
				Ticket ticket = ticketOpt.get();
			}
			return ticketOpt;
		} finally {
			ticketGetSpan.tag("peer.service", "mysql");
			ticketGetSpan.annotate("Client received");
			ticketGetSpan.finish();
		}
		
	}
	
	public Optional<Status> getStatusByStatusCode(String statusCode) {
		Optional<Status> status = statusService.getStatusByCode(statusCode);
		if (status.isEmpty()) {
			return null;
		}
		return status;
	}
	
	public Ticket createTicket(Ticket ticket) {
		ticket = ticketRepository.save(ticket);
		ticket.setTicketCode(this.createTicketCode(ticket.getId()));
		ticket = ticketRepository.save(ticket);
		return ticket;
	}
	
	public Ticket updateTicket(Ticket ticket) {
		Optional<Ticket> existingTicket = ticketRepository.findById(ticket.getId());
		if (existingTicket.isPresent()) {
			ticket = ticketRepository.save(ticket);
			return ticket;
		}
		return null;
	}
	
	public void deleteTicket(String code) {
		try {
			ticketRepository.deleteByTicketCode(code);
		} catch (Exception e) {
			//log exception
		}
	}
}
