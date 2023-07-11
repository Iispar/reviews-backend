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
		// TODO: calc new average for item
		// TODO: calc new topwords
		return true;
	}
	
	/**
	 * API GET call to /api/review/get?userId=(input)&page=(input)&sort=(input)
	 * will return the reviews for that user. This will be used in the latest on the home page
	 * for the latest reviews. This also sorts the reviews from latest.
	 * @param {int} id
	 * 		  The user id that searches for reviews.
	 * @param {int} page
	 * 		  The page you want reviews from
	 * @return latest reviews for userId from index (from) to index (to).
	 */
	@GetMapping("/get")
	public List<Review> getReviewsForUser(
			@RequestParam("userId") int id,
			@RequestParam("page") int page) {
		List<Review> reviews = reviewService.getReviewsForUser(id, page);
		if (reviews.isEmpty()) {
			throw new IllegalStateException(
					"found no reviews with user id");
		}
		return reviews;
	}

	/**
	 * API GET call to /api/review/get/search?title=(input)&itemId=(input)&sort=(input)&page=(input)
	 * will return the reviews that match the inputted title search. This will be used in the items
	 * reviews component as search.
	 * @param {String} title
	 * 	      Title that was searched.
	 * @param {int} id
	 * 		  the id of the item the reviews correspond to.
	 * @param {String} sort
	 * 		  The sort used for search
	 * @param {int} page
	 * 		  The page you want results of.
	 * @return reviews that match the title and id of search
	 */
	@GetMapping("/get/search")
	public List<Review> getReviewsWithTitleForItem(
			@RequestParam("title") String title,
			@RequestParam("itemId") int id,
			@RequestParam("sort") String sort,
			@RequestParam("page") int page){
		
		// if sort different call...
		List<Review> reviews = reviewService.getReviewsWithTitleForItem(title, id, sort, page);
		if (reviews.isEmpty()) {
			throw new IllegalStateException(
					"found no reviews with item id and name");
		}
		return reviews;
	}
	
	/**
	 * API GET call to /api/review/get/chart?userId=(input)&time=(input) will return the
	 * corresponding data for the chart component.
	 * @param {int} id
	 * 	      Id of the user you wish to get results for.
	 * @param {string} time
	 * 		  Either month or week, the selection for grouping of results.
	 * @return count of reviews and their avg rating grouped by parameter.
	 */
	@GetMapping("/get/chart")
	public List<Object> getChartForUser(
			@RequestParam("userId") int id,
			@RequestParam("time") String time) {
	List<Object> res = reviewService.getChartForUser(time, id);
	return res;
	}

	/**
	 * API DELETE call to /api/review/del?reviewId=(input) will delete the review that
	 * corresponds with the inputted reviewId.
	 * @param {int} id
	 * 	      Id of the review you wish to delete.
	 * @return true id successful, false Otherwise.
	 */
	@DeleteMapping("/del")
	public boolean deleteReview(@RequestParam("reviewId") int id) {
		if (Boolean.TRUE.equals(reviewService.deleteReview(id))) return true;
		return false;
	}
}
