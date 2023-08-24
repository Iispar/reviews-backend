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
        if (this.getRating() > o.getRating()) {
            return 1;
        }
        else if (this.getRating() < o.getRating()) {
            return -1;
        }
        else return 0;
    }
}
