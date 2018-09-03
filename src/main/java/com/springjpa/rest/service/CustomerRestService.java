package com.springjpa.rest.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.springjpa.bean.CustomerBean;
import com.springjpa.dao.CustomerDao;
import com.springjpa.model.Customer;
import com.springjpa.rest.model.MessageInfo;
import com.springjpa.service.CustomerService;

@Service
@Transactional
public class CustomerRestService {
	
	@Autowired 
	CustomerDao customerDao;
	@Autowired 
	CustomerService customerService;
	
	public ResponseEntity<MessageInfo> customerGetAll(MessageInfo messageInfo) {

		List<Customer> customerList = customerService.listCustomer();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		if (customerList.isEmpty()) {
			messageInfo.setCode(HttpStatus.BAD_REQUEST.toString());
			messageInfo.setStatus(HttpStatus.BAD_REQUEST);
			messageInfo.setMessage("List Customer Is Empty");
			messageInfo.setResult(customerList);
			return new ResponseEntity<>(messageInfo, HttpStatus.BAD_REQUEST);
		}

		messageInfo.setCode(HttpStatus.OK.toString());
		messageInfo.setStatus(HttpStatus.OK);
		messageInfo.setMessage("Success");
		messageInfo.setResult(customerList);
		return new ResponseEntity<>(messageInfo, HttpStatus.OK);
	}

	public ResponseEntity<String> customerGetId(long id) {
		
		Customer customerById = customerService.findByCustomerId(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		ResponseEntity<String> responseEntity = null;

		String body = "";
		Gson gson = new Gson();
		if (customerById == null) {
			body = "null";
		} else {
			body = gson.toJson(customerById);
		}

		if (customerById == null) {
			responseEntity = new ResponseEntity<String>(body, headers, HttpStatus.NOT_FOUND);
		}

		if (customerById != null) {
			responseEntity = new ResponseEntity<String>(body, headers, HttpStatus.OK);
		}

		return responseEntity;
	}

	public ResponseEntity<MessageInfo> createCustomer(CustomerBean customerBean,MessageInfo messageInfo) {
		
		if (customerService.existsByName(customerBean.getFirstName())) {
			messageInfo.setCode(HttpStatus.ALREADY_REPORTED.toString());
			messageInfo.setStatus(HttpStatus.ALREADY_REPORTED);
			messageInfo.setMessage("Conflic Duplicate firstName");
			return new ResponseEntity<>(messageInfo, HttpStatus.ALREADY_REPORTED);
		}
		if (customerService.isRecordFull()){
			messageInfo.setCode(HttpStatus.ALREADY_REPORTED.toString());
			messageInfo.setStatus(HttpStatus.ALREADY_REPORTED);
			messageInfo.setMessage("Record Customer Full");
			return new ResponseEntity<>(messageInfo, HttpStatus.ALREADY_REPORTED);
		}
		customerService.saveCustomer(customerBean);
		
		CustomerBean newCustomerBean = new CustomerBean();
		Customer customer = customerService.findByFirstName(customerBean.getFirstName());
		BeanUtils.copyProperties(customer, newCustomerBean);
		
		messageInfo.setCode(HttpStatus.CREATED.toString());
		messageInfo.setStatus(HttpStatus.CREATED);
		messageInfo.setMessage("Success");
		messageInfo.setResult(newCustomerBean);
		return new ResponseEntity<>(messageInfo, HttpStatus.CREATED);
	}

	public ResponseEntity<MessageInfo> updateCustomer(CustomerBean customerBean,MessageInfo messageInfo) {
		
		Customer customer = customerService.findByCustomerId(customerBean.getId());

		if (customer == null) {
			System.out.println("Customer with name " + customerBean.getFirstName() + " not found");
			messageInfo.setCode(HttpStatus.ALREADY_REPORTED.toString());
			messageInfo.setStatus(HttpStatus.ALREADY_REPORTED);
			messageInfo.setMessage("Customer with name " + customerBean.getFirstName() + " not found");
			messageInfo.setResult(customer);
			return new ResponseEntity<MessageInfo>(messageInfo, HttpStatus.ALREADY_REPORTED);
		}
		
		customerService.updateCustomer(customerBean.getId(), customerBean);
		messageInfo.setCode(HttpStatus.OK.toString());
		messageInfo.setStatus(HttpStatus.OK);
		messageInfo.setMessage("Customer with Name " + customerBean.getFirstName() + " Success update");
		messageInfo.setResult(customerBean);

		return new ResponseEntity<MessageInfo>(messageInfo, HttpStatus.OK);
	}

	public ResponseEntity<MessageInfo> deleteCustomer(long id, @Valid MessageInfo messageInfo) {

		Customer customer = customerService.findByCustomerId(id);
		if (customer == null) {
			System.out.println("Unable to delete. Customer with name " + customer.getFirstName() + " not found");
			messageInfo.setCode(HttpStatus.ALREADY_REPORTED.toString());
			messageInfo.setStatus(HttpStatus.ALREADY_REPORTED);
			messageInfo.setMessage("Unable to delete. Customer with name " + customer.getFirstName() + " not found");
			messageInfo.setResult(customer);
			return new ResponseEntity<MessageInfo>(HttpStatus.ALREADY_REPORTED);
		}
		CustomerBean customerBean = new CustomerBean();
		BeanUtils.copyProperties(customer, customerBean);
		customerService.deleteById(id);
		messageInfo.setCode(HttpStatus.OK.toString());
		messageInfo.setStatus(HttpStatus.OK);
		messageInfo.setMessage("Customer with name " + customer.getFirstName() + " Success delete");
		messageInfo.setResult(customerBean);
		return new ResponseEntity<MessageInfo>(messageInfo, HttpStatus.OK);
	}
}
