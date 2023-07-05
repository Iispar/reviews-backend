package com.example.reviewsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.reviewsbackend.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

}
