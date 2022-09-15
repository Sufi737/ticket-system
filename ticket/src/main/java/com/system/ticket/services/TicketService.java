package com.system.ticket.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.system.ticket.entities.Ticket;
import com.system.ticket.entities.TicketRestRequest;
import com.system.ticket.repositories.TicketRepository;

@Service
public class TicketService {

	@Autowired
	private TicketRepository ticketRepository;
	
	@Value("${ticket.prefix}")
	private String ticketCodePrefix;
	
	private String createTicketCode(Integer ticketId) {
		return ticketCodePrefix+ticketId;
	}
	
	public Optional<Ticket> getTicketByCode(String ticketCode) {
		return ticketRepository.findByTicketCode(ticketCode);
	}
	
	public Ticket createTicket(TicketRestRequest ticketRequest) {
		Ticket ticket = new Ticket();
		ticket.setTitle(ticketRequest.getTitle());
		ticket.setDescription(ticketRequest.getDescription());
		ticket.setCreatedBy(ticketRequest.getCreatedBy());
		ticket.setAssignedTo(ticketRequest.getAssignedTo());
		ticket.setCreatedAt(ticketRequest.getCreatedAt());
		ticket.setUpdatedAt(ticketRequest.getUpdatedAt());
		ticket = ticketRepository.save(ticket);
		ticket.setTicketCode(this.createTicketCode(ticket.getId()));
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
