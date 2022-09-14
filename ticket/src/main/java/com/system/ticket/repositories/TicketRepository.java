package com.system.ticket.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.system.ticket.entities.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Integer>{

	public Optional<Ticket> findByTicketCode(String ticketCode);
	
	public void deleteByTicketCode(String ticketCode);
}
