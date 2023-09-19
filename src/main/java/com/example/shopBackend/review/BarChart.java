package com.example.shopBackend.review;


/**
 * Interface to return an object for the barChart.
 * @author iiro
 *
 */
@SuppressWarnings("unused")
public interface BarChart extends Comparable<BarChart> {
    int getRating();
    double getCount();

    @Override
    default int compareTo(BarChart o) {
        return Integer.compare(this.getRating(), o.getRating());
    }
}
