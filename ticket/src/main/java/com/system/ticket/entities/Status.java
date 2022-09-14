package com.system.ticket.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="status")
public class Status {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="entity_id")
	private Integer id;
	
	@Column(name="status_code")
	private String statusCode;
	
	@Column(name="status_name")
	private String statusName;
}
