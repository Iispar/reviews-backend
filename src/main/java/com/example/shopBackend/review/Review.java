package com.example.shopBackend.review;

import java.sql.Date;

import com.example.shopBackend.customer.Customer;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.rating.Rating;
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
 * The Review entity in the backend.
 * @author iiro
 *
 */
@Entity(name="Review")
@Table(name="review", schema="reviews_schema")
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id", nullable = false, updatable = false, unique = true)
	private int id;
	
	@Column(name = "review_date", nullable = false)
	private Date date;
	@Column(name = "review_body", nullable = false)
	private String body;
	@Column(name = "review_title", nullable = false)
	private String title;
	@Column(name = "review_likes", nullable = true)
	private String likes;
	@Column(name = "review_dislikes", nullable = true)
	private String dislikes;
	
	// reference to customer entity - unidirectional.
    @ManyToOne
	@JoinColumn(name = "review_user", referencedColumnName = "user_id", nullable = false)
	private Customer user;

	// reference to rating entity - unidirectional.
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    @OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "review_rating", referencedColumnName = "rating_id", nullable = false)
	private Rating rating;

	// reference to item entity - unidirectional.
    @ManyToOne
	@JoinColumn(name = "review_item", referencedColumnName = "item_id", nullable = false)
	private Item item;
	
	public Review(Date date, String body, String title, String likes, String dislikes, Customer user, Rating rating, Item item) {
		this.date = date;
		this.body = body;
		this.title = title;
		this.likes = likes;
		this.dislikes = dislikes;
		this.user = user;
		this.rating = rating;
		this.item = item;
	}

	public Review(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public String getDislikes() {
		return dislikes;
	}

	public void setDislikes(String dislikes) {
		this.dislikes = dislikes;
	}

	public Customer getUser() {
		return user;
	}

	public void setUser(Customer user) {
		this.user = user;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}