package com.system.ticket.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.system.ticket.entities.Ticket;
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
	
	public Ticket createTicket(Ticket ticket) {
		Optional<Ticket> existingTicket = ticketRepository.findByTicketCode(ticket.getTicketCode());
		if (existingTicket.isPresent()) {
			return null;
		}
		ticket.setTicketCode(this.createTicketCode(ticket.getId()));
		ticket = ticketRepository.save(ticket);
		return ticket;
	}
	
	public Ticket updateDepartment(Ticket ticket) {
		Optional<Ticket> existingTicket = ticketRepository.findById(ticket.getId());
		if (existingTicket.isPresent()) {
			ticket = ticketRepository.save(ticket);
			return ticket;
		}
		return null;
	}
	
	public void deleteTicket(Integer id) {
		try {
			ticketRepository.deleteById(id);
		} catch (Exception e) {
			//log exception
		}
	}
}
