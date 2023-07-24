package com.example.shopBackend.review;

import java.util.ArrayList;
import java.util.List;

public class RatedReviews {
    private List<SingleRatedReview> reviews = new ArrayList<SingleRatedReview>();
    private List<Object> topPos = new ArrayList<Object>();
    private List<Object> topNeg = new ArrayList<Object>();

    public RatedReviews(List<SingleRatedReview> reviews, List<Object> topPos, List<Object> topNeg) {
        this.reviews = reviews;
        this.topPos = topPos;
        this.topNeg = topNeg;
    }

    public RatedReviews() {};

    public List<SingleRatedReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<SingleRatedReview> reviews) {
        this.reviews = reviews;
    }

    public List<Object> getTopPos() {
        return topPos;
    }

    public void setTopPos(List<Object> topPos) {
        this.topPos = topPos;
    }

    public List<Object> getTopNeg() {
        return topNeg;
    }

    public void setTopNeg(List<Object> topNeg) {
        this.topNeg = topNeg;
    }
}
