package com.example.shopBackend.review;

import com.example.shopBackend.account.Account;
import com.example.shopBackend.item.Item;
import jakarta.persistence.*;

import java.sql.Date;

/**
 * The Review entity in the backend.
 * @author iiro
 *
 */
@SuppressWarnings("unused")
@Entity(name="Review")
@Table(name="reviews", schema="reviews_schema")
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id", nullable = false, updatable = false, unique = true)
	private int id;
	
	@Column(name = "review_date")
	private Date date;

	@Lob
	@Column(name = "review_body", nullable = false, columnDefinition = "text")
	private String body;

	@Lob
	@Column(name = "review_title", nullable = false, columnDefinition = "text")
	private String title;
	@Column(name = "review_likes")
	private Integer likes;
	@Column(name = "review_dislikes")
	private Integer dislikes;
	@Column(name = "review_rating")
	private int rating;
	
	// reference to customer entity - unidirectional.
    @ManyToOne
	@JoinColumn(name = "review_account", referencedColumnName = "account_id", nullable = false)
	private Account account;

	// reference to item entity - unidirectional.
    @ManyToOne
	@JoinColumn(name = "review_item", referencedColumnName = "item_id", nullable = false)
	private Item item;

	public Review(Date date, String body, String title, Integer likes, int dislikes, Account account, int rating, Item item) {
		this.date = date;
		this.body = body;
		this.title = title;
		this.likes = likes;
		this.dislikes = dislikes;
		this.account = account;
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

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}