package com.example.shopBackend.review;

import java.util.List;

/**
 * Class to include data for rated reviews.
 */
@SuppressWarnings("unused")
public class RatedReviews {
    private List<SingleRatedReview> reviews;

    public RatedReviews(List<SingleRatedReview> reviews) {
        this.reviews = reviews;
    }

    public RatedReviews() {}

    public List<SingleRatedReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<SingleRatedReview> reviews) {
        this.reviews = reviews;
    }
}
