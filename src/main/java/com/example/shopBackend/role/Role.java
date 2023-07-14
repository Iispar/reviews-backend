package com.example.shopBackend.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name="Role")
@Table(name="role", schema="reviews_schema")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "role_id", nullable = false, updatable = false, unique = true)
	private int id;
	
	@Column(name = "role_name", nullable = false)
	private String name;

	public Role(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Role() {};

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
	
	
}
