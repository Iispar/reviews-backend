package com.example.shopBackend.review;

/**
 * Class for all the data in a single rated review
 */
@SuppressWarnings("unused")
public class SingleRatedReview {
    private String review;
    private int star;

    public SingleRatedReview(String review, int star) {
        this.review = review;
        this.star = star;
    }

    public SingleRatedReview() {}

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
