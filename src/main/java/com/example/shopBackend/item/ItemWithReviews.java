package com.example.shopBackend.item;
/**
 * Interface to return an object for the chart.
 * @author iiro
 *
 */
@SuppressWarnings("unused")
public interface ItemWithReviews {
    double getId();

    int getRating();

    String getTitle();

    String getReviews();
}