package com.example.shopBackend.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	public Review saveReview(Review review) {
		return reviewRepository.save(review);
	}
	
	public List<Review> getAllReviews() {
		return reviewRepository.findAll();
	}
	
	public List<Review> getReviewsForUser(int id) {
		return reviewRepository.findAllUserId(id);
	}
	
	public Boolean deleteReview(int id) {
		reviewRepository.deleteById(id);
		return true;
	}
}
