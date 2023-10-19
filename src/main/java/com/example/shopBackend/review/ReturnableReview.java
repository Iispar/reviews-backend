package com.example.shopBackend.review;

import java.sql.Date;

public class ReturnableReview {

    private int id;
    private Date date;
    private String body;
    private String title;
    private Integer likes;
    private Integer dislikes;
    private int rating;
    private int accountId;
    private int itemId;

    public ReturnableReview(int id, Date date, String body, String title, Integer likes, int dislikes, int accountId, int rating, int itemId) {
        this.id = id;
        this.date = date;
        this.body = body;
        this.title = title;
        this.likes = likes;
        this.dislikes = dislikes;
        this.accountId = accountId;
        this.rating = rating;
        this.itemId = itemId;
    }

    public ReturnableReview(){}

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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

}
