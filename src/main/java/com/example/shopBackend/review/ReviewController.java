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
	
	@PostMapping("/add")
	public String add(@RequestBody Review review) {
		reviewService.saveReview(review);
		return "Succesfully added review";
	}
	
	@GetMapping("/get")
	public List<Review> getAllReviews(@RequestParam("userId") int id) {
		List<Review> reviews = reviewService.getReviewsForUser(id);
		if (reviews.isEmpty()) {
			throw new IllegalStateException(
					"found no reviews with user id");
		}
		return reviews;
	}
	
	@DeleteMapping("/del")
	public String deleteReview(@RequestParam("reviewId") int id) {
		if (reviewService.deleteReview(id)) return "Deleted succesfully";
		return "Deletion failed";
	}
}
