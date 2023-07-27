package com.example.shopBackend.pages;

import com.example.shopBackend.review.Chart;
import com.example.shopBackend.review.Review;

import java.util.List;

public class ItemPage {
    private List<Review> latestReviews;
    private List<Chart> chart;
    private List<String> topPos;
    private List<String> topNeg;

    public ItemPage(List<Review> latestReviews, List<Chart> chart, List<String> topPos, List<String> topNeg) {
        this.latestReviews = latestReviews;
        this.chart = chart;
        this.topPos = topPos;
        this.topNeg = topNeg;
    }

    public List<Review> getLatestReviews() {
        return latestReviews;
    }

    public void setLatestReviews(List<Review> latestReviews) {
        this.latestReviews = latestReviews;
    }

    public List<Chart> getChart() {
        return chart;
    }

    public void setChart(List<Chart> chart) {
        this.chart = chart;
    }

    public List<String> getTopPos() {
        return topPos;
    }

    public void setTopPos(List<String> topPos) {
        this.topPos = topPos;
    }

    public List<String> getTopNeg() {
        return topNeg;
    }

    public void setTopNeg(List<String> topNeg) {
        this.topNeg = topNeg;
    }
}
