package com.system.ticket.entities;

import java.sql.Date;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TicketRestRequest {

	private String title;
	private String description;
	private String createdBy;
	private String assignedTo;
	private Date createdAt;
	private Date updatedAt;
	private String statusCode;
	
}
