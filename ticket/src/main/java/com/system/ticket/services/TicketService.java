package com.system.ticket.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	Logger logger = LoggerFactory.getLogger(TicketService.class);
	
	public String createTicketCode(Integer ticketId) {
		return ticketCodePrefix+ticketId;
	}
	
	public Optional<Ticket> getTicketByCode(String ticketCode) {
		ScopedSpan ticketSpan = tracer.startScopedSpan("getTicketDatabaseCall");
		try {
			Optional<Ticket> ticketOpt = ticketRepository.findByTicketCode(ticketCode);
			if (ticketOpt.isPresent()) {
				Ticket ticket = ticketOpt.get();
			}
			return ticketOpt;
		} catch (Exception e) {
			logger.debug("Exception getting ticket by code: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			ticketSpan.tag("peer.service", "mysql");
			ticketSpan.annotate("Client received");
			ticketSpan.finish();
		}
		return null;
	}
	
	public Optional<Status> getStatusByStatusCode(String statusCode) {
		ScopedSpan statusGetSpan = tracer.startScopedSpan("getStatusDatabaseCall");
		try {
			Optional<Status> status = statusService.getStatusByCode(statusCode);
			if (status.isEmpty()) {
				return null;
			}
			return status;
		} catch (Exception e) {
			logger.debug("Exception getting status by code: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			statusGetSpan.tag("peer.service", "mysql");
			statusGetSpan.annotate("Client received");
			statusGetSpan.finish();
		}
		return null;
		
	}
	
	public Ticket createTicket(Ticket ticket) {
		ScopedSpan ticketSpan = tracer.startScopedSpan("createTicketDatabaseCall");
		try {
			ticket = ticketRepository.save(ticket);
			ticket.setTicketCode(this.createTicketCode(ticket.getId()));
			ticket = ticketRepository.save(ticket);
			return ticket;
		} catch (Exception e) {
			logger.debug("Exception creating ticket: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			ticketSpan.tag("peer.service", "mysql");
			ticketSpan.annotate("Client received");
			ticketSpan.finish();
		}
		return null;
	}
	
	public Ticket updateTicket(Ticket ticket) {
		ScopedSpan ticketSpan = tracer.startScopedSpan("updateTicketDatabaseCall");
		try {
			Optional<Ticket> existingTicket = ticketRepository.findById(ticket.getId());
			if (existingTicket.isPresent()) {
				ticket = ticketRepository.save(ticket);
				return ticket;
			}
		} catch (Exception e) {
			logger.debug("Exception updating ticket: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			ticketSpan.tag("peer.service", "mysql");
			ticketSpan.annotate("Client received");
			ticketSpan.finish();
		}
		return null;
	}
	
	public void deleteTicket(String code) {
		ScopedSpan ticketSpan = tracer.startScopedSpan("deleteTicketDatabaseCall");
		try {
			ticketRepository.deleteByTicketCode(code);
		} catch (Exception e) {
			logger.debug("Exception deleting ticket: "+e.getMessage());
			logger.debug("Exception trace: "+e.getStackTrace());
		} finally {
			ticketSpan.tag("peer.service", "mysql");
			ticketSpan.annotate("Client received");
			ticketSpan.finish();
		}
	}
}
