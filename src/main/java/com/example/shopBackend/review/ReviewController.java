package com.example.shopBackend.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller for the review table
 * @author iiro
 *
 */
@RestController
@RequestMapping("/api/review")
public class ReviewController {

	private final ReviewService reviewService;
	
	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	/**
	 * API GET call to /api/review/add with content in the body that describes the added review.
	 * Will add it to the database. Used in the frontend item page with add review.
	 *
	 * @param review
	 *        The review to be added to the database
	 * @return True if successful. False otherwise
	 */
	@PostMapping("/add")
	public List<Review> add(@RequestBody List<Review> review) {
		return reviewService.saveAllReviews(review);
	}
	
	/**
	 * API GET call to /api/review/get/user?userId=(input)&page=(input)&sort=(input)
	 * will return the reviews for that user. This will be used in the latest on the home page
	 * for the latest reviews. This also sorts the reviews from latest.
	 * @param id
	 * 		  The user id that searches for reviews.
	 * @param page
	 * 		  The page you want reviews from
	 * @return latest reviews for userId from index (from) to index (to).
	 */
	@GetMapping("/get/user")
	public List<Review> getReviewsForUser(
			@RequestParam("userId") int id,
			@RequestParam("page") int page) {
		List<Review> reviews = reviewService.getReviewsForUser(id, page, "review_date", "asc");
		if (reviews.isEmpty()) {
			throw new IllegalStateException(
					"found no reviews with user id");
		}
		return reviews;
	}

	/**
	 * API GET call to /api/review/get/user?itemId=(input)&page=(input)&sort=(input)
	 * will return the reviews for that item. This will be used in the latest on the home page
	 * for the latest reviews. This also sorts the reviews from latest.
	 * @param id
	 * 		  The item id that searches for reviews.
	 * @param page
	 * 		  The page you want reviews from
	 * @return latest reviews for userId from index (from) to index (to).
	 */
	@GetMapping("/get/item")
	public List<Review> getReviewsForItem(
			@RequestParam("itemId") int id,
			@RequestParam("page") int page,
			@RequestParam("sort") String sort,
			@RequestParam("sortDir") String sortDir) {
		List<Review> reviews = reviewService.getReviewsForItem(id, page, sort, sortDir);
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
	 * @param title
	 * 	      Title that was searched.
	 * @param id
	 * 		  the id of the item the reviews correspond to.
	 * @param sort
	 * 		  The sort used for search
	 * @param page
	 * 		  The page you want results of.
	 * @return reviews that match the title and id of search
	 */
	@GetMapping("/get/search")
	public List<Review> getReviewsWithTitleForItem(
			@RequestParam("title") String title,
			@RequestParam("itemId") int id,
			@RequestParam("sort") String sort,
			@RequestParam("sortDir") String sortDir,
			@RequestParam("page") int page){
		
		List<Review> reviews = reviewService.getReviewsWithTitleForItem(title, page, id, sort, sortDir);
		if (reviews.isEmpty()) {
			throw new IllegalStateException(
					"found no reviews with item id and name");
		}
		return reviews;
	}
	
	/**
	 * API GET call to /api/review/get/chart?userId=(input)&time=(input) will return the
	 * corresponding data for the chart component.
	 * @param id
	 * 	      id of the user you wish to get results for.
	 * @param time
	 * 		  Either month or week, the selection for grouping of results.
	 * @return chart for user.
	 */
	@GetMapping("/get/chart/user")
	public List<Chart> getChartForUser(
			@RequestParam("userId") int id,
			@RequestParam("time") String time) {
	return reviewService.getChartForUser(time, id);
	}
	
	/**
	 * API GET call to /api/review/get/chart?itemId=(input)&time=(input) will return the
	 * corresponding data for the chart component.
	 * @param id
	 * 	      id of the item you wish to get results for.
	 * @param time
	 * 		  Either month or week, the selection for grouping of results.
	 * @return chart for item
	 */
	@GetMapping("/get/chart/item")
	public List<Chart> getChartForItem(
			@RequestParam("itemId") int id,
			@RequestParam("time") String time) {
	return reviewService.getChartForItem(time, id);
	}

	/**
	 * API DELETE call to /api/review/del?reviewId=(input) will delete the review that
	 * corresponds with the inputted reviewId.
	 * @param id
	 * 	      id of the review you wish to delete.
	 * @return true id successful, false Otherwise.
	 */
	@DeleteMapping("/del")
	public boolean deleteReview(@RequestParam("reviewId") int id) {
		return Boolean.TRUE.equals(reviewService.deleteReview(id));
	}
}
