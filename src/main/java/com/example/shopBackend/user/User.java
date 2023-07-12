package com.example.shopBackend.user;

import com.example.shopBackend.role.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The User entity
 * @author iiro
 *
 */
@Entity(name="User")
@Table(name="user", schema="reviews_schema")
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
	
	// reference to role entity - unidirectional.
    @ManyToOne
	@JoinColumn(name = "user_role", referencedColumnName = "role_id", nullable = false)
	private Role role;

	public User(int id, String name, String username, String password, String email, Role role) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	
}
