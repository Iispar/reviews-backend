package com.example.shopBackend.words;

import java.util.List;

import com.example.shopBackend.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * The Words entity in the backend.
 * @author iiro
 *
 */
@Entity(name="Words")
@Table(name="words", schema="reviews_schema")
public class Words {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "words_id", nullable = false, updatable = false, unique = true)
	private int id;
	
	@Column(name = "words_positive", nullable = true, updatable = true, unique = false)
	private List<String> positive;
	
	@Column(name = "words_negative", nullable = true, updatable = true, unique = false)
	private List<String> negative;
	
	public Words(int id, List<String> positive, List<String> negative) {
		this.id = id;
		this.positive = positive;
		this.negative = negative;
	};
	
	public Words() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getPositive() {
		return positive;
	}

	public void setPositive(List<String> positive) {
		this.positive = positive;
	}

	public List<String> getNegative() {
		return negative;
	}

	public void setNegative(List<String> negative) {
		this.negative = negative;
	}
}