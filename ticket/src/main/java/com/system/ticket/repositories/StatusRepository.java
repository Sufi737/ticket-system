package com.system.ticket.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.system.ticket.entities.Status;

public interface StatusRepository extends CrudRepository<Status, Integer>{
	Optional<Status> findByStatusCode(String statusCode);
}
