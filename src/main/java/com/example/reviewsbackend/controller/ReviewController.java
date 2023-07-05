package com.example.reviewsbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reviewsbackend.model.Review;
import com.example.reviewsbackend.service.ReviewService;

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
}
