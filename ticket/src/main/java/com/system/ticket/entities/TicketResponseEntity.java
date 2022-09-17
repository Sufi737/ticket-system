package com.system.ticket.entities;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketResponseEntity {
	private Integer id;
	private String ticketCode;
	private String title;
	private String description;
	private EmployeeResponseEntity createdBy;
	private EmployeeResponseEntity assignedTo;
	private Date createdAt;
	private Date updatedAt;
	private Status status;
}
