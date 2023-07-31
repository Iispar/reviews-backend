package com.example.shopBackend.pages;

import com.example.shopBackend.item.Item;
import com.example.shopBackend.review.Chart;
import com.example.shopBackend.review.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to include all wanted data for home page.
 */
@SuppressWarnings("unused")
public class Homepage {
    private List<Review> latestReviews = new ArrayList<>();
    private List<Item> topItems = new ArrayList<>();

    private float ratingsAvg;
    private int itemsCount;
    private int reviewsCount;

    private List<Chart> chart;

    public Homepage(List<Review> latestReviews, List<Item> topItems, float ratingsAvg, int itemsCount, int reviewsCount, List<Chart> chart) {
        this.latestReviews = latestReviews;
        this.topItems = topItems;
        this.ratingsAvg = ratingsAvg;
        this.itemsCount = itemsCount;
        this.reviewsCount = reviewsCount;
        this.chart = chart;
    }

    public Homepage() {}

    public List<Review> getLatestReviews() {
        return latestReviews;
    }

    public void setLatestReviews(List<Review> latestReviews) {
        this.latestReviews = latestReviews;
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
