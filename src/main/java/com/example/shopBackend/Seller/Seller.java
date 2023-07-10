package com.example.shopBackend.seller;

import com.example.shopBackend.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * The Seller entity in the backend.
 * @author iiro
 *
 */
@Entity(name="Seller")
@Table(name="seller", schema="reviews_schema")
public class Seller extends User {
    
	@Column(name = "user_company", nullable = false)
	private String company;

	public Seller(int id, String name, String username, String password, String email, String company) {
		super(id, name, username, password, email);
		this.company = company;
	}
	
	public Seller() {}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	};
}