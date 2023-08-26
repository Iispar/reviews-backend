package com.example.shopBackend.review;

/**
 * Interface to return an object for the chart.
 * @author iiro
 *
 */
@SuppressWarnings("unused")
public interface Chart extends Comparable<Chart> {
	double getRating();
	int getCount();
	String getTime();
	String getTimeYear();

	@Override
	default int compareTo(Chart o) {
		return this.getTime().compareTo(o.getTime());
	}
}
