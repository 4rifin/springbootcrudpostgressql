package com.springjpa.rest.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.springjpa.bean.CustomerBean;
import com.springjpa.rest.model.MessageInfo;
import com.springjpa.rest.service.CustomerRestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RestController
@RequestMapping(value = "/ws")
@Api(value="Customer Rest", description = "Api Retrieve Data Costumer")
public class CustomerRestController {

	static Logger log = Logger.getLogger(CustomerRestController.class.getName());

	@Autowired
	CustomerRestService customerRestService;

	// --------------------------------------------------------
	// ----------------Retrieve All Customers------------------
	// --------------------------------------------------------
	@ApiOperation(value = "Get All Data Customer")
	@RequestMapping(value = "/customerGetAll", method = RequestMethod.GET)
	public ResponseEntity<MessageInfo> listCustomerGetAll(@Valid MessageInfo messageInfo) {

		try {
			ResponseEntity<MessageInfo> messageResult = customerRestService.customerGetAll(messageInfo);
			return messageResult;
		} catch (Exception e) {
			throw new UserIsSelfException("message");
		}
	}

	// --------------------------------------------------------
	// ----------------Retrieve Single Customers---------------
	// --------------------------------------------------------
	@ApiOperation(value = "Get Data Customer By id")
	@RequestMapping(value = "/customerGetId/{id}", method = RequestMethod.GET)
	public ResponseEntity<String> listCustomers(@PathVariable("id") long id) {

		try {
			ResponseEntity<String> messageResult = customerRestService.customerGetId(id);
			return messageResult;
		} catch (Exception e) {
			throw new UserIsSelfException("message");
		}
	}

	// --------------------------------------------------------
	// ----------------Create a Customer-----------------------
	// --------------------------------------------------------
	@ApiOperation(value = "add Data Customer")
	@RequestMapping(value = "/customer/add", method = RequestMethod.POST)
	public ResponseEntity<MessageInfo> createCustomer(@RequestBody CustomerBean customerBean,
			@Valid MessageInfo messageInfo) {

		try {
			ResponseEntity<MessageInfo> messageResult = customerRestService.createCustomer(customerBean, messageInfo);
			return messageResult;
		} catch (Exception e) {
			throw new UserIsSelfException("message");
		}
	}

	// --------------------------------------------------------
	// ----------------Update a Customer-----------------------
	// --------------------------------------------------------
	@ApiOperation(value = "Edit Data Customer")
	@RequestMapping(value = "/customer/edit", method = RequestMethod.PUT)
	public ResponseEntity<MessageInfo> updateCustomer(@RequestBody CustomerBean customerBean,
			@Valid MessageInfo messageInfo) {

		try {
			ResponseEntity<MessageInfo> messageResult = customerRestService.updateCustomer(customerBean, messageInfo);
			return messageResult;
		} catch (Exception e) {
			throw new UserIsSelfException("message");
		}
	}

	// --------------------------------------------------------
	// ----------------Delete a Customer-----------------------
	// --------------------------------------------------------
	@ApiOperation(value = "Delete Data Customer")
	@RequestMapping(value = "/customer/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<MessageInfo> deleteCustomer(@PathVariable("id") long id, @Valid MessageInfo messageInfo) {

		try {
			ResponseEntity<MessageInfo> messageResult = customerRestService.deleteCustomer(id, messageInfo);
			return messageResult;
		} catch (Exception e) {
			throw new UserIsSelfException("message");
		}
	}

	/*
	 * ========================================================================
	 */
	@ApiOperation(value = "Get All Data Customer list")
	@RequestMapping(value = "/listCustomer", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<MessageInfo> listCustomer(@Valid MessageInfo messageInfo) {

		try {
			ResponseEntity<MessageInfo> messageResult = customerRestService.customerGetAll(messageInfo);
			return messageResult;
		} catch (Exception e) {
			throw new UserIsSelfException("message");
		}
	}

	@ApiOperation(value = "Get Data Customer By id List ")
	@RequestMapping(value = "/listCustomer/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> listCustomerById(@PathVariable("id") long id) {

		try {
			ResponseEntity<String> messageResult = customerRestService.customerGetId(id);
			return messageResult;
		} catch (Exception e) {
			throw new UserIsSelfException("message");
		}
	}

	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Can't add yourself") // 403
	public class UserIsSelfException extends RuntimeException {
		private static final long serialVersionUID = -6871307095006922960L;

		public UserIsSelfException(String message) {
			super(message);
		}
	}
}
