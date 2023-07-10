package com.example.shopBackend.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The Item entity in the backend.
 * @author iiro
 *
 */
@Entity(name="Category")
@Table(name="item_category", schema="reviews_schema")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false, updatable = false, unique = true)
	private int id;
	
	@Column(name = "category_name", nullable = false, updatable = false, unique = true)
	private String category;
	
	
	public Category(int id, String category) {
		this.id = id;
		this.category = category;
	}
	
	public Category() {};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
}
