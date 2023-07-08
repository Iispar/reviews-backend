package com.example.shopBackend.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

	private ReviewService reviewService;
	
	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	/**
	 * API GET call to /api/review/add with content in the body that describes the added review.
	 * Will add it to the database. Used in the frontend item page with add review.
	 * @param {Review} review
	 * 	      The review to be added to the database
	 * @return True if successful. False otherwise
	 */
	@PostMapping("/add")
	public Boolean add(@RequestBody Review review) {
		reviewService.saveReview(review);
		return true;
	}
	
	/**
	 * API GET call to /api/review/get?userId=(input) will return the reviews for that user.
	 * This will be used in the latest on the home page.
	 * @param id
	 * @return
	 */
	@GetMapping("/get")
	public List<Review> getReviewsForUser(
			@RequestParam("userId") int id,
			@RequestParam("from") int from,
			@RequestParam("to") int to) {
		List<Review> reviews = reviewService.getReviewsForUser(id, from, to);
		if (reviews.isEmpty()) {
			throw new IllegalStateException(
					"found no reviews with user id");
		}
		return reviews;
	}

	/**
	 * API GET call to /api/review/get/search?title=(input)&userId=(input) will return the
	 * reviews that match the inputted title search. This will be used in the items
	 * reviews component as search.
	 * @param {String} title
	 * 	      Title that was searched.
	 * @param {int} id
	 * 		  the id of the item the reviews correspond to.
	 * @return reviews that match the title and id of search
	 */
	@GetMapping("/get/search")
	public List<Review> getReviewsWithTitleForUser(
			@RequestParam("title") String title,
			@RequestParam("itemId") int id) {
		
		// if sort different call...
		List<Review> reviews = reviewService.getReviewsWithTitleForUser(title, id);
		if (reviews.isEmpty()) {
			throw new IllegalStateException(
					"found no reviews with item id and name");
		}
		return reviews;
	}
	
	/**
	 * API DELETE call to /api/review/del?reviewId=(input) will delete the review that
	 * corresponds with the inputted reviewId.
	 * @param {int} id
	 * 	      Id of the review we wish to delete.
	 * @return true id successful, false Otherwise.
	 */
	@DeleteMapping("/del")
	public boolean deleteReview(@RequestParam("reviewId") int id) {
		if (Boolean.TRUE.equals(reviewService.deleteReview(id))) return true;
		return false;
	}
}
