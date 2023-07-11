package com.example.shopBackend.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	/**
	 * Saves a new review to the database.
	 * @param {Review} review
	 * 		  The review to be added to the database.
	 * @return
	 */
	public Review saveReview(Review review) {
		return reviewRepository.save(review);
	}
	
	/**
	 * Finds all reviews for users items for page from the database. And returns them.
	 * @param {int} id
	 * 		  The id of the user you want reviews for.
	 * @param {int} page
	 * 		  The page you want to receive
	 * @return reviews that match query.
	 */
	public List<Review> getReviewsForUser(int id, int page) {
		Pageable pageRequest = PageRequest.of(page, 4);
		return reviewRepository.findAllUserId(id, pageRequest);
	}
	
	/**
	 * Deletes a review with the corresponding review_id.
	 * @param {int} id
	 * 		  The id of the review to be deleted.
	 * @return true if successful, false otherwise.
	 */
	public Boolean deleteReview(int id) {
		reviewRepository.deleteById(id);
		// false if fails?
		return true;
	}
	
	/**
	 * Finds all reviews for item with title from the database and returns them.
	 * @param {String} title
	 * 		  The searched title.
	 * @param {int} id
	 * 		  The id of the item 
	 * @param {String} sort
	 * 		  The sort used for search
	 * @param {int} page
	 * 		  The page you want to receive
	 * @return reviews that match query.
	 */
	public List<Review> getReviewsWithTitleForItem(String title, int id, String sort, int page) {
		String formattedTitle = String.format("%%%s%%", title).replace("_", "%");
		Pageable pageRequest = PageRequest.of(page, 4, Sort.by(sort).ascending());
		return reviewRepository.findAllByTitleForItem(formattedTitle, id, pageRequest);
	}
	
	/**
	 * Finds the count of reviews and their average rating by user id and returns them
	 * Different query depending on the wanted grouping.
	 * @param {int} id
	 * 	      Id of the user you wish to get results for.
	 * @param {string} time
	 * 		  Either month or week, the selection for grouping of results.
	 * @return count of reviews and their avg rating grouped by parameter.
	 */
	public List<Object> getChartForUser(String time, int id) {
		List<Object> res = null;
		if (time .equals("month")) {
			res =  reviewRepository.findChartForUserByMonth(id);
		} else if (time.equals("week")) {
			res =  reviewRepository.findChartForUserByWeek(id);
		}
		return res;
	}
	
	/**
	 * Finds the count of reviews and their average rating by item id and returns them
	 * Different query depending on the wanted grouping.
	 * @param {int} id
	 * 	      Id of the item you wish to get results for.
	 * @param {string} time
	 * 		  Either month or week, the selection for grouping of results.
	 * @return count of reviews and their avg rating grouped by parameter.
	 */
	public List<Object> getChartForItem(String time, int id) {
		List<Object> res = null;
		if (time .equals("month")) {
			res =  reviewRepository.findChartForItemByMonth(id);
		} else if (time.equals("week")) {
			res =  reviewRepository.findChartForItemByWeek(id);
		}
		return res;
	}
}
