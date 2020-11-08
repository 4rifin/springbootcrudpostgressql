package com.springjpa.bean;

import io.swagger.annotations.ApiModelProperty;

public class CustomerBean {
	private long id;
	@ApiModelProperty(notes="First Name Customer")
	private String firstName;
	@ApiModelProperty(notes="Last Name Customer")
	private String lastName;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
}
