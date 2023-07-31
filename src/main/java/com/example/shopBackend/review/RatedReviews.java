package com.example.shopBackend.review;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to include data for rated reviews.
 */
@SuppressWarnings("unused")
public class RatedReviews {
    private List<SingleRatedReview> reviews = new ArrayList<>();
    private List<String> topPos = new ArrayList<>();
    private List<String> topNeg = new ArrayList<>();

    public RatedReviews(List<SingleRatedReview> reviews, List<String> topPos, List<String> topNeg) {
        this.reviews = reviews;
        this.topPos = topPos;
        this.topNeg = topNeg;
    }

    public RatedReviews() {}

    public List<SingleRatedReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<SingleRatedReview> reviews) {
        this.reviews = reviews;
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
