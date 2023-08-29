package com.example.shopBackend.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repo for the review table.
 * @author iiro
 *
 */
@Repository
public interface ReviewRepository extends PagingAndSortingRepository<Review, Integer>, JpaRepository<Review, Integer> {

	/**
	 * Query to find all reviews for Account.
	 * @param id
	 * 		  The Account id that searched.
	 * @param pageable
	 * 		  The pageable object that selects page.
	 * @return the items that matched the query
	 */
	@Query(value = "SELECT * FROM reviews r WHERE r.review_item IN"
			+ "(SELECT item_id FROM items i WHERE i.item_account = ?1)", nativeQuery = true)
	List<Review> findAllAccountId(int id, Pageable pageable);
	
	/**
	 * Query to find all reviews for item.
	 * @param id
	 * 		  The item id that searched.
	 * @param pageable
	 * 		  The pageable object that selects page.
	 * @return the items that matched the query
	 */
	@Query(value = "SELECT * FROM reviews r WHERE r.review_item = ?1", nativeQuery = true)
	List<Review> findAllItemId(int id, Pageable pageable);
	
	/**
	 * Query to find all reviews with searched title or body for an item.
	 * @param search
	 * 		  The searched words.
	 * @param id
	 * 	 	  The item id that is searched with.
	 * @param pageable
	 * 		  The pageable object that selects page and sort.
	 * @return the items that matched the query
	 */
	@Query(value = "SELECT * FROM reviews r WHERE r.review_item = ?2 AND (r.review_title LIKE ?1 OR r.review_body LIKE ?1)", nativeQuery = true)
	List<Review> findAllBySearchForItem(String search, int id, Pageable pageable);
	
	/**
	 * Query first all the items that match the Accounts id and then get their reviews.
	 * Count the result, calc average of the rating attr and group results by month.
	 * @param id
	 * 	      id of the Account you wish to get results for.
	 * @return list of chart with count of reviews and their avg rating grouped by month.
	 */
	@Query(value = "SELECT COUNT(review_rating) AS count, AVG(review_rating) AS rating, MONTHNAME(review_date) AS time, min(YEAR(review_date)) AS timeYear FROM reviews r WHERE r.review_item IN"
			+ "(SELECT item_id FROM items i WHERE i.item_account = ?1)"
			+ "GROUP BY MONTHNAME(review_date)", nativeQuery = true)
	List<Chart> findChartForAccountByMonth(int id);
	
	/**
	 * Query first all the items that match the Accounts id and then get their reviews.
	 * Count the result, calc average of the rating attr and group results by week.
	 * @param id
	 * 	      id of the Account you wish to get results for.
	 * @return list of count of reviews and their avg rating grouped by week.
	 */
	@Query(value = "SELECT COUNT(review_rating) AS count, AVG(review_rating) AS rating, WEEK(review_date) AS time, min(YEAR(review_date)) AS timeYear FROM reviews r WHERE r.review_item IN"
			+ "(SELECT item_id FROM items i WHERE i.item_account = ?1)"
			+ "GROUP BY WEEK(review_date)", nativeQuery = true)
	List<Chart> findChartForAccountByWeek(int id);
	
	/**
	 * Query first all the reviews that match the item id.
	 * Count the result, calc average of the rating attr and group results by month.
	 * @param id
	 * 	      id of the item you wish to get results for.
	 * @return list of count of reviews and their avg rating grouped by month.
	 */
	@Query(value = "SELECT COUNT(review_rating) AS count, AVG(review_rating) AS rating, MONTHNAME(review_date) AS time, min(YEAR(review_date)) AS timeYear FROM reviews r WHERE r.review_item = ?1 GROUP BY MONTHNAME(review_date)", nativeQuery = true)
	List<Chart> findChartForItemByMonth(int id);
	
	/**
	 * Query first all the reviews that match the item id.
	 * Count the result, calc average of the rating attr and group results by week.
	 * @param id
	 * 	      id of the item you wish to get results for.
	 * @return list of count of reviews and their avg rating grouped by week.
	 */
	@Query(value = "SELECT COUNT(review_rating) AS count, AVG(review_rating) AS rating, WEEK(review_date) AS time, min(YEAR(review_date)) AS timeYear FROM reviews r WHERE r.review_item = ?1 GROUP BY WEEK(review_date)", nativeQuery = true)
	List<Chart> findChartForItemByWeek(int id);

	/**
	 * Queries all the reviews that match the item id and returns all the
	 * review bodies.
	 * @param id
	 * 	      id of the item you wish to get results for.
	 * @return body of the all reviews that match the item id.
	 */
	@Query(value = "SELECT review_body FROM reviews r WHERE r.review_item = ?1", nativeQuery = true)
	List<String> findAllBodysWithItemId(int id);

	/**
	 * Queries all the reviews that match the item id and returns all the
	 * review ratings.
	 * @param id
	 * 	      id of the item you wish to get results for.
	 * @return rating of the all reviews that match the item id.
	 */
	@Query(value = "SELECT review_rating FROM reviews r WHERE r.review_item = ?1", nativeQuery = true)
	List<Integer> findAllRatingsWithItemId(int id);

	/**
	 * Queries all the reviews that match the item id and returns all the counts grouped by their rating value
	 * @param id
	 * 	      id of the item you wish to get results for.
	 * @return list of barChart with count of all ratings with same rating
	 */
	@Query(value = "SELECT COUNT(*) as count, review_rating as rating FROM reviews r WHERE r.review_item IN (SELECT item_id FROM items i WHERE i.item_account = ?1) GROUP BY review_rating", nativeQuery = true)
	List<BarChart> findRatingDistributionWithAccountId(int id);

	/**
	 * Queries all the reviews that match the Account id and returns all the
	 * count of review
	 * @param id
	 * 	      id of the Account you wish to get results for.
	 * @return count of all matches
	 */
	@Query(value = "SELECT COUNT(*) FROM reviews r WHERE r.review_item IN (SELECT item_id FROM items i WHERE i.item_account = ?1)", nativeQuery = true)
	int findCountWithAccountId(int id);

	/**
	 * Queries the count of reviews for item with selected id.
	 * @param id
	 * 		  Selected item id.
	 * @return count of reviews that match the query.
	 */
	@Query(value = "SELECT COUNT(*) FROM reviews r WHERE r.review_item = ?1", nativeQuery = true)
	int findReviewCountForItem(int id);

	/**
	 * Queries the count of reviews that are positive for item with selected id.
	 * @param id
	 * 		  Selected item id.
	 * @return count of reviews that match the query.
	 */
	@Query(value = "SELECT COUNT(*) FROM reviews r WHERE r.review_item = ?1 AND r.review_rating > 3", nativeQuery = true)
	int findPosReviewCountForItem(int id);

	/**
	 * Queries the count of reviews that are negative for item with selected id.
	 * @param id
	 * 		  Selected item id.
	 * @return count of reviews that match the query.
	 */
	@Query(value = "SELECT COUNT(*) FROM reviews r WHERE r.review_item = ?1 AND r.review_rating < 3", nativeQuery = true)
	int findNegReviewCountForItem(int id);
}

