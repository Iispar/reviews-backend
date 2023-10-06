package com.example.shopBackend.item;

public class ReturnableItem {
    private int id;

    private String title;


    private int accountId;

    private float rating;

    private int categoryId;

    public ReturnableItem(int id, String title, int accountId, float rating, int categoryId) {
        this.id = id;
        this.title = title;
        this.accountId = accountId;
        this.rating = rating;
        this.categoryId = categoryId;
    }

    public ReturnableItem() {};

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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
