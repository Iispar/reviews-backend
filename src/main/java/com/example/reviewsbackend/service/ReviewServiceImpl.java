package com.example.reviewsbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reviewsbackend.model.Review;
import com.example.reviewsbackend.repository.ReviewRepository;

@Service
public class ReviewServiceImpl implements ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Override
	public Review saveReview(Review review) {
		return reviewRepository.save(review);
	}
}
