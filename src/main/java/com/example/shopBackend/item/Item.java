package com.example.shopBackend.item;

import com.example.shopBackend.category.Category;
import com.example.shopBackend.user.User;
import com.example.shopBackend.words.Words;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

/**
 * The Item entity in the backend.
 * @author iiro
 *
 */
@Entity(name="Item")
@Table(name="items", schema="reviews_schema")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id", nullable = false, updatable = false, unique = true)
	private int id;
	
	@Column(name = "item_title", nullable = false)
	private String title;

	@Column(name = "item_desc", nullable = false)
	private String desc;
	
	// reference to seller entity - unidirectional.
    @ManyToOne
	@JoinColumn(name = "item_account", referencedColumnName = "account_id", nullable = false)
	private User user;
	
	@Column(name = "item_rating", nullable = true)
	private float rating;
	
	// reference to category entity - unidirectional.
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    @ManyToOne
	@JoinColumn(name = "item_category", referencedColumnName = "category_id", nullable = false)
	private Category category;
    
	// reference to words entity - unidirectional.
    @OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "item_words", referencedColumnName = "words_id", nullable = true)
	private Words words;
	
	public Item(String title, User user, float rating, Category category, Words words, String desc) {
		this.title = title;
		this.user = user;
		this.rating = rating;
		this.category = category;
		this.words = words;
		this.desc = desc;
	}

	public Item() {};

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	
}
