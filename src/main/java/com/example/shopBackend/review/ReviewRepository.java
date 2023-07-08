package com.example.shopBackend.review;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends PagingAndSortingRepository<Review, Integer>, JpaRepository<Review, Integer> {
	
	/**
	 * Query to find all reviews for user from index to index.
	 * @param {int} id
	 * 		  The user id that searched.
	 * @return the items that matched the query
	 */
	@Query(value = "SELECT * FROM Review r WHERE r.review_item IN"
			+ "(SELECT item_id FROM Item i WHERE i.item_user = ?1)", nativeQuery = true)
	List<Review> findAllUserId(int id, Pageable pageable);
	
	/**
	 * Query to find all reviews with searched title for a item.
	 * @param {String} title
	 * 		  The searched title.
	 * @param {int} id
	 * 	 	  The item id that is searched with.
	 * @return the items that matched the query
	 */
	@Query(value = "SELECT * FROM Review r WHERE r.review_title LIKE ?1 AND r.review_item = ?2", nativeQuery = true)
	List<Review> findAllByTitleForItem(String title, int id, Pageable pageable);
}

