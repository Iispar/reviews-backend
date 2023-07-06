package com.example.shopBackend.review;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="review", schema="reviews_schema")
public class Review {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "review_id")
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
	
	
}