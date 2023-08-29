package com.example.shopBackend.pages;

import com.example.shopBackend.review.Chart;
import com.example.shopBackend.review.Review;

import java.util.List;

/**
 * Class to include all wanted data for item page.
 */
@SuppressWarnings("unused")
public class ItemPage {

    private String title;
    private int reviewsCount;
    private int positiveReviews;
    private int negativeReviews;
    private double rating;

    private List<Review> reviews;
    private List<Chart> chart;
    private List<String> topPos;
    private List<String> topNeg;

    public ItemPage(String title, int reviewsCount, int positiveReviews, int negativeReviews, double rating, List<Review> reviews, List<Chart> chart, List<String> topPos, List<String> topNeg) {
        this.title = title;
        this.reviewsCount = reviewsCount;
        this.positiveReviews = positiveReviews;
        this.negativeReviews = negativeReviews;
        this.rating = rating;
        this.reviews = reviews;
        this.chart = chart;
        this.topPos = topPos;
        this.topNeg = topNeg;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviews(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public int getPositiveReviews() {
        return positiveReviews;
    }

    public void setPositiveReviews(int positiveReviews) {
        this.positiveReviews = positiveReviews;
    }

    public int getNegativeReviews() {
        return negativeReviews;
    }

    public void setNegativeReviews(int negativeReviews) {
        this.negativeReviews = negativeReviews;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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
