package com.example.shopBackend.pages;

import com.example.shopBackend.item.ReturnableItem;
import com.example.shopBackend.response.ListRes;
import com.example.shopBackend.review.BarChart;
import com.example.shopBackend.review.Chart;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to include all wanted data for home page.
 */
@SuppressWarnings("unused")
public class Homepage {

    private String accountName;
    private ListRes latestReviews;
    private List<ReturnableItem> topItems = new ArrayList<>();

    private float ratingsAvg;
    private int itemsCount;
    private int reviewsCount;

    private List<BarChart> barChart;

    private List<Chart> chart;

    public Homepage(String accountName, ListRes latestReviews, List<ReturnableItem> topItems, float ratingsAvg, int itemsCount, int reviewsCount, List<BarChart> barChart, List<Chart> chart) {
        this.accountName = accountName;
        this.latestReviews = latestReviews;
        this.topItems = topItems;
        this.ratingsAvg = ratingsAvg;
        this.itemsCount = itemsCount;
        this.reviewsCount = reviewsCount;
        this.barChart = barChart;
        this.chart = chart;
    }

    public List<BarChart> getBarChart() {
        return barChart;
    }

    public void setBarChart(List<BarChart> barChart) {
        this.barChart = barChart;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public ListRes getLatestReviews() {
        return latestReviews;
    }

    public void setLatestReviews(ListRes latestReviews) {
        this.latestReviews = latestReviews;
    }

    public List<ReturnableItem> getTopItems() {
        return topItems;
    }

    public void setTopItems(List<ReturnableItem> topItems) {
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
