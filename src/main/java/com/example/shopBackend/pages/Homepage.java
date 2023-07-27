package com.example.shopBackend.pages;

import com.example.shopBackend.item.Item;
import com.example.shopBackend.review.Chart;
import com.example.shopBackend.review.Review;

import java.util.ArrayList;
import java.util.List;

public class Homepage {
    private List<Review> latesReviews = new ArrayList<Review>();
    private List<Item> topItems = new ArrayList<Item>();

    private float ratingsAvg;
    private int itemsCount;
    private int reviewsCount;

    private List<Chart> chart;

    public Homepage(List<Review> latesReviews, List<Item> topItems, float ratingsAvg, int itemsCount, int reviewsCount, List<Chart> chart) {
        this.latesReviews = latesReviews;
        this.topItems = topItems;
        this.ratingsAvg = ratingsAvg;
        this.itemsCount = itemsCount;
        this.reviewsCount = reviewsCount;
        this.chart = chart;
    }

    public Homepage() {}

    public List<Review> getLatesReviews() {
        return latesReviews;
    }

    public void setLatesReviews(List<Review> latesReviews) {
        this.latesReviews = latesReviews;
    }

    public List<Item> getTopItems() {
        return topItems;
    }

    public void setTopItems(List<Item> topItems) {
        this.topItems = topItems;
    }

    public float getRatingsAvg() {
        return ratingsAvg;
    }

    public void setRatingsAvg(float ratingsAvg) {
        this.ratingsAvg = ratingsAvg;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public List<Chart> getChart() {
        return chart;
    }

    public void setChart(List<Chart> chart) {
        this.chart = chart;
    }
}
