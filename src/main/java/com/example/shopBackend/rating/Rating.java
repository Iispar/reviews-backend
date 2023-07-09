package com.example.shopBackend.rating;

import com.example.shopBackend.review.Review;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * The Rating entity in the backend.
 * @author iiro
 *
 */
@Entity(name="Rating")
@Table(name="rating", schema="reviews_schema")
public class Rating {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rating_id", nullable = false, updatable = false, unique = true)
	private int id;
	
	@Column(name = "rating_value", nullable = false)
	private int value;

	@Column(name = "rating_item", nullable = true)
	private int item;
	

	@OneToOne(mappedBy="rating")
	private Review review;
	
    public Rating(int id, int value, int item, Review review) {
		this.id = id;
		this.value = value;
		this.item = item;
		this.review = review;
	}
    
    public Rating() {};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}
}
