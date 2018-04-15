package com.springjpa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springjpa.model.Customer;

public interface CustomerDao extends CrudRepository<Customer, Long>{
	
	public List<Customer> findByLastName(String lastName);
	@Query("select u from Customer u where u.id = ?1")
	public Customer findByCustomerId(long id);
	public List<Customer> findAllByOrderByIdAsc();
}
