package com.example.shopBackend.item;

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
@Entity(name="Item")
@Table(name="item", schema="reviews_schema")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id", nullable = false, updatable = false, unique = true)
	private int id;
	
	@Column(name = "item_title", nullable = false)
	private String title;
	
	@Column(name = "item_user", nullable = false)
	private int user;
	
	@Column(name = "item_rating", nullable = false)
	private String rating;
	
	@Column(name = "item_category", nullable = false)
	private int category;
	
	

	public Item(int id, String title, int user, String rating, int category) {
		this.id = id;
		this.title = title;
		this.user = user;
		this.rating = rating;
		this.category = category;
	}
	
	public Item() {};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}
	
	
}
