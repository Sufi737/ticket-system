package com.system.ticket.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TicketRestRequest {

	private String title;
	private String description;
	private String createdBy;
	private String assignedTo;
	private String createdAt;
	private String updatedAt;
	private String status;
}
