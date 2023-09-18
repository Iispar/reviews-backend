package com.example.shopBackend.review;

import com.example.shopBackend.response.ListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
	 * API GET call to /api/review/add with content in the body that describes the added review
	 * will add it to the database. Used in the frontend item page with add review.
	 *
	 * @param review
	 *        The review to be added to the database
	 * @return True if successful. Error otherwise
	 */
	@PreAuthorize("@authorization.addReviewsAreOwn(authentication, #review)")
	@PostMapping("/add")
	public List<Review> add(@RequestBody List<Review> review) {
		return reviewService.saveAllReviews(review);
	}
	
	/**
	 * API GET call to /api/review/get/account?accountId=(input)&page=(input)
	 * will return the reviews for that Account. Reviews are sorted in ascending date
	 * @param id
	 * 		  The Account id that searches for reviews.
	 * @param page
	 * 		  The page you want reviews from
	 * @return selected reviews for account in date ascending order.
	 */
	@PreAuthorize("#id == authentication.principal.id")
	@GetMapping("/get/account")
	public List<Review> getReviewsForAccount(
			@RequestParam("accountId") int id,
			@RequestParam("page") int page) {
		return reviewService.getReviewsForAccount(id, page, "review_date", "asc");
	}

	/**
	 * API GET call to /api/review/get/item?itemId=(input)&page=(input)&sort=(input)&sortDir=(input)
	 * will return the reviews for that item. Sorts the reviews as specified with sort and sortDir.
	 * @param id
	 * 		  The item id that searches for reviews.
	 * @param sort
	 * 		  The sort used for search
	 * @param sortDir
	 * 		  The direction of the sort
	 * @param page
	 * 		  The page you want results of.
	 * @return selected reviews for item
	 */
	@PreAuthorize("@authorization.isOwnItem(authentication, #id)")
	@GetMapping("/get/item")
	public ListRes getReviewsForItem(
			@RequestParam("itemId") int id,
			@RequestParam("page") int page,
			@RequestParam("sort") String sort,
			@RequestParam("sortDir") String sortDir) {
		return reviewService.getReviewsForItem(id, page, sort, sortDir);
	}

	/**
	 * API GET call to /api/review/get/search?search=(input)&itemId=(input)&sort=(input)&sortDir=(input)&page=(input)
	 * will return the reviews that match the inputted search. Sorted as determined with sort and sortDir.
	 * @param search
	 * 	      Words that was searched.
	 * @param id
	 * 		  the id of the item the reviews correspond to.
	 * @param sort
	 * 		  The sort used for search
	 * @param sortDir
	 * 		  The direction of the sort
	 * @param page
	 * 		  The page you want results of.
	 * @return reviews that match the title and id of search
	 */
	@PreAuthorize("@authorization.isOwnItem(authentication, #id)")
	@GetMapping("/get/search")
	public ListRes getReviewsWithTitleForItem(
			@RequestParam("search") String search,
			@RequestParam("itemId") int id,
			@RequestParam("sort") String sort,
			@RequestParam("sortDir") String sortDir,
			@RequestParam("page") int page){
		
		return reviewService.getReviewsWithSearchForItem(search, id, page, sort, sortDir);
	}
	
	/**
	 * API GET call to /api/review/get/chart/account?accountId=(input)&time=(input) will return the
	 * corresponding data for the chart component.
	 * @param id
	 * 	      id of the Account you wish to get results for.
	 * @param time
	 * 		  Either month or week, the selection for grouping of results.
	 * @return chart for Account.
	 */
	@PreAuthorize("#id == authentication.principal.id")
	@GetMapping("/get/chart/account")
	public List<Chart> getChartForAccount(
			@RequestParam("accountId") int id,
			@RequestParam("time") String time) {
	return reviewService.getChartForAccount(time, id);
	}
	
	/**
	 * API GET call to /api/review/get/chart/item?itemId=(input)&time=(input) will return the
	 * corresponding data for the chart component.
	 * @param id
	 * 	      id of the item you wish to get results for.
	 * @param time
	 * 		  Either month or week, the selection for grouping of results.
	 * @return chart for item
	 */
	@PreAuthorize("@authorization.isOwnItem(authentication, #id)")
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
	@PreAuthorize("@authorization.isOwnReview(authentication, #id)")
	@DeleteMapping("/del")
	public boolean deleteReview(@RequestParam("reviewId") int id) {
		return Boolean.TRUE.equals(reviewService.deleteReview(id));
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleIllegalState(IllegalStateException illegalStateException) {
		return illegalStateException.getMessage();
	}
}
