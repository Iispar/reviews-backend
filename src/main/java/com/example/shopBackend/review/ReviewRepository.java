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
	 * Query to find all reviews for users for page.
	 * @param {int} id
	 * 		  The user id that searched.
	 * @param {Pageable} pageable
	 * 		  The pageable object that selects page.
	 * @return the items that matched the query
	 */
	@Query(value = "SELECT * FROM Review r WHERE r.review_item IN"
			+ "(SELECT item_id FROM Item i WHERE i.item_user = ?1)", nativeQuery = true)
	List<Review> findAllUserId(int id, Pageable pageable);
	
	/**
	 * Query to find all reviews with searched title for a item returns selected page and sorted in what 
	 * is described in the pageable object.
	 * @param {String} title
	 * 		  The searched title.
	 * @param {int} id
	 * 	 	  The item id that is searched with.
	 * @param {Pageable} pageable
	 * 		  The pageable object that selects page and sort.
	 * @return the items that matched the query
	 */
	@Query(value = "SELECT * FROM Review r WHERE r.review_title LIKE ?1 AND r.review_item = ?2", nativeQuery = true)
	List<Review> findAllByTitleForItem(String title, int id, Pageable pageable);
	
	/**
	 * Query first all the items that match the users id and then get their reviews.
	 * Count the result, calc average of the rating attr and group results by month.
	 * @param {int} id
	 * 	      Id of the user you wish to get results for.
	 * @return count of reviews and their avg rating grouped by month.
	 */
	@Query(value = ""
			+ "SELECT COUNT(review_rating), AVG(review_rating) FROM Review r WHERE r.review_item IN"
			+ "(SELECT item_id FROM Item i WHERE i.item_user = ?1)"
			+ "GROUP BY MONTH(review_date)", nativeQuery = true)
	List<Object> findChartForUserByMonth(int id);
	
	/**
	 * Query first all the items that match the users id and then get their reviews.
	 * Count the result, calc average of the rating attr and group results by week.
	 * @param {int} id
	 * 	      Id of the user you wish to get results for.
	 * @return count of reviews and their avg rating grouped by week.
	 */
	@Query(value = ""
			+ "SELECT COUNT(review_rating), AVG(review_rating) FROM Review r WHERE r.review_item IN"
			+ "(SELECT item_id FROM Item i WHERE i.item_user = ?1)"
			+ "GROUP BY WEEK(review_date)", nativeQuery = true)
	List<Object> findChartForUserByWeek(int id);
	
	/**
	 * Query first all the reviews that match the item id.
	 * Count the result, calc average of the rating attr and group results by month.
	 * @param {int} id
	 * 	      Id of the item you wish to get results for.
	 * @return count of reviews and their avg rating grouped by month.
	 */
	@Query(value = "SELECT COUNT(review_rating), AVG(review_rating) FROM Review r WHERE r.review_item = ?1 GROUP BY MONTH(review_date)", nativeQuery = true)
	List<Object> findChartForItemByMonth(int id);
	
	/**
	 * Query first all the reviews that match the item id.
	 * Count the result, calc average of the rating attr and group results by week.
	 * @param {int} id
	 * 	      Id of the item you wish to get results for.
	 * @return count of reviews and their avg rating grouped by week.
	 */
	@Query(value = "SELECT COUNT(review_rating), AVG(review_rating) FROM Review r WHERE r.review_item = ?1 GROUP BY WEEK(review_date)", nativeQuery = true)
	List<Object> findChartForItemByWeek(int id);
}

