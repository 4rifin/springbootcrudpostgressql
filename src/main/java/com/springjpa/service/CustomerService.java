package com.springjpa.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springjpa.bean.CustomerBean;
import com.springjpa.dao.CustomerDao;
import com.springjpa.model.Customer;

@Service
@Transactional
public class CustomerService {
	
	@Autowired 
	CustomerDao customerDao;
	
	public List<Customer> listCustomer(){
		List<Customer> customerList =  (List<Customer>) customerDao.findAllByOrderByIdAsc();
		return customerList;
	}
	
	public void saveCustomer(CustomerBean customer){
		Customer newCustomer = new Customer();
		newCustomer.setFirstName(customer.getFirstName());
		newCustomer.setLastName(customer.getLastName());
		customerDao.save(newCustomer);
	}
	
	public Customer findByCustomerId(long id){
		return customerDao.findByCustomerId(id);
	}
	
	public void deleteById(long id){
		customerDao.deleteById(id);
	}
	
	public void updateCustomer(long id,CustomerBean customer){
		Customer editCustomer = findByCustomerId(id);
		editCustomer.setFirstName(customer.getFirstName());
		editCustomer.setLastName(customer.getLastName());
		customerDao.save(editCustomer);
	}
	public Boolean isRecordFull(){
		List<Customer>listCustomer = (List<Customer>) customerDao.findAll();
		if(listCustomer.size() >= 10){
			return true;
		}
		return false;
	}
	
}
