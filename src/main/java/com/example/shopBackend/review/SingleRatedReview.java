package com.example.shopBackend.review;

public class SingleRatedReview {
    private String review;
    private String star;

    public SingleRatedReview(String review, String star) {
        this.review = review;
        this.star = star;
    }

    public SingleRatedReview() {};

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
