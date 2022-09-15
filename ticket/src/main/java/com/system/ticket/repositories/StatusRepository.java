package com.system.ticket.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.system.ticket.entities.Status;

public interface StatusRepository extends CrudRepository<Status, Integer>{
	public Optional<Status> findByStatusCode(String statusCode);
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM Status s where s.statusCode = ?1")
	public void deleteByStatusCode(String statusCode);
}
