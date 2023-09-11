package com.example.shopBackend.review;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

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
		Pattern pattern = Pattern.compile("^-?\\d+$");
		final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("MMMM").withLocale(Locale.ENGLISH);

		if (parseInt(this.getTimeYear()) > parseInt(o.getTimeYear())) return 1;
		else if (parseInt(this.getTimeYear()) < parseInt(o.getTimeYear())) return -1;
		else {
			if (pattern.matcher(this.getTime()).matches()) {
				return Integer.compare(parseInt(this.getTime()), parseInt(o.getTime()));
			}
			else {
				return Integer.compare(
						dtFormatter.parse(this.getTime()).get(ChronoField.MONTH_OF_YEAR),
						dtFormatter.parse(o.getTime()).get(ChronoField.MONTH_OF_YEAR)
				);
			}
		}
	}
}
