package com.example.shopBackend.item;

import com.example.shopBackend.account.Account;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.words.Words;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

/**
 * The Item entity in the backend.
 * @author iiro
 *
 */
@SuppressWarnings("unused")
@Entity(name="Item")
@Table(name="items", schema="reviews_schema")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id", nullable = false, updatable = false, unique = true)
	private int id;
	
	@Column(name = "item_title", nullable = false)
	private String title;
	
	// reference to seller entity - unidirectional.
    @ManyToOne
	@JoinColumn(name = "item_account", referencedColumnName = "account_id", nullable = false)
	private Account account;
	
	@Column(name = "item_rating")
	private float rating;
	
	// reference to category entity - unidirectional.
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    @ManyToOne
	@JoinColumn(name = "item_category", referencedColumnName = "category_id", nullable = false)
	private Category category;
    
	// reference to words entity - unidirectional.
    @OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "item_words", referencedColumnName = "words_id")
	private Words words;

	public Item() {}

	public Item(int id, String title, Account account, float rating, Category category, Words words) {
		this.id = id;
		this.title = title;
		this.account = account;
		this.rating = rating;
		this.category = category;
		this.words = words;
	}

    public Item(String title, Account account, float rating, Category category, Words words) {
		this.title = title;
		this.account = account;
		this.rating = rating;
		this.category = category;
		this.words = words;
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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
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
