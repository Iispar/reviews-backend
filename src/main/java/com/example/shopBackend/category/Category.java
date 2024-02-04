package com.example.shopBackend.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The item category entity in the backend.
 * @author iiro
 *
 */
@Entity(name="Category")
@Table(name="item_categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false, updatable = false, unique = true)
	private int id;
	
	@Column(name = "category_name", nullable = false, updatable = false)
	private String name;
	
	
	public Category(String name) {
		this.name = name;
	}
	
	public Category() {}

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
