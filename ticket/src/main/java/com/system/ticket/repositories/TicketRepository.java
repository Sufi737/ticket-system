package com.system.ticket.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.system.ticket.entities.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Integer>{

	public Optional<Ticket> findByTicketCode(String ticketCode);
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM Ticket t where t.ticketCode = ?1")
	public void deleteByTicketCode(String ticketCode);
}
