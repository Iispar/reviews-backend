package com.example.shopBackend.customer;

import com.example.shopBackend.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * The Customer entity in the backend.
 * @author iiro
 *
 */
@Entity(name="Customer")
@Table(name="customer", schema="reviews_schema")
public class Customer extends User {
    
	@Column(name = "user_custom", nullable = false)
	private String custom;

	public Customer(int id, String name, String username, String password, String email, String custom) {
		super(id, name, username, password, email);
		this.custom = custom;
	}	
	
	public Customer() {}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}
}
