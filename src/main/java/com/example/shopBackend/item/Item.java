package com.example.shopBackend.item;

import com.example.shopBackend.category.Category;
import com.example.shopBackend.seller.Seller;
import com.example.shopBackend.words.Words;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
	
	// reference to seller entity - unidirectional.
    @ManyToOne
	@JoinColumn(name = "item_user", referencedColumnName = "user_id", nullable = false)
	private Seller user;
	
	@Column(name = "item_rating", nullable = false)
	private String rating;
	
	// reference to category entity - unidirectional.
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    @ManyToOne
	@JoinColumn(name = "item_category", referencedColumnName = "category_id", nullable = false)
	private Category category;
    
	// reference to words entity - bidirectional.
    @OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "item_words", referencedColumnName = "words_id", nullable = false)
	private Words words;
	
	public Item(int id, String title, Seller user, String rating, Category category, Words words) {
		this.id = id;
		this.title = title;
		this.user = user;
		this.rating = rating;
		this.category = category;
		this.words = words;
	}

	public Item() {};

	public Words getWords() {
		return words;
	}

	public void setWords(Words words) {
		this.words = words;
	}

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

	public Seller getUser() {
		return user;
	}

	public void setUser(Seller user) {
		this.user = user;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	
}
