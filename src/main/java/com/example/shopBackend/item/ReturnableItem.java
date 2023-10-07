package com.example.shopBackend.item;

public class ReturnableItem {
    private int id;

    private String title;


    private int accountId;

    private float rating;

    private String categoryName;

    public ReturnableItem(int id, String title, int accountId, float rating, String categoryName) {
        this.id = id;
        this.title = title;
        this.accountId = accountId;
        this.rating = rating;
        this.categoryName = categoryName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryId) {
        this.categoryName = categoryName;
    }
}
