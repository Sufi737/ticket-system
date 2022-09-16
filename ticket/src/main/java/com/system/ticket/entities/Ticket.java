package com.system.ticket.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name="ticket")
public class Ticket {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="entity_id")
	private Integer id;
	
	@Column(name="ticket_code")
	private String ticketCode;
	
	@Column(name="title")
	private String title;
	
	@Column(name="description")
	private String description;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="assigned_to")
	private String assignedTo;
	
	@Column(name="created_at", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name="updated_at", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	@Column(name="status_id")
	private Integer statusId;
	
}
