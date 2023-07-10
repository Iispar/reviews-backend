package com.example.shopBackend.user;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * The User class. Is the parent class for Customer and Seller.
 * @author iiro
 *
 */
@MappedSuperclass
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "user_id", nullable = false, updatable = false, unique = true)
	private int id;
	
	@Column(name = "user_name", nullable = false)
	private String name;
	
	@Column(name = "user_username", nullable = false)
	private String username;
	
	@Column(name = "user_password", nullable = false)
	private String password;
	
	@Column(name = "user_email", nullable = false)
	private String email;

	public User(int id, String name, String username, String password, String email) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public User() {};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String pass) {
		this.password = pass;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
