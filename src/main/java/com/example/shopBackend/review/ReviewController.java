package com.example.shopBackend.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	@PostMapping("/add")
	public String add(@RequestBody Review review) {
		reviewService.saveReview(review);
		return "Succesfully added review";
	}
	
	@GetMapping("/getAll")
	public List<Review> getAllReviews() {
		return reviewService.getAllReviews();
	}
}
