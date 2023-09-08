package com.example.shopBackend.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repo for the item table
 * @author iiro
 *
 */
@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, Integer>, JpaRepository<Item, Integer> {

	
	/**
	 * Query to find all items for Account with id
	 * @param id
	 * 		  The Account id that searched.
	 * @param pageable
	 * 		  The pageable object that selects page and sorts.
	 * @return the items that matched the query
	 */
	@Query(value = "SELECT * FROM items i WHERE i.item_account = ?1", nativeQuery = true)
	List<Item> findAllAccountId(int id, Pageable pageable);

	/**
	 * Query to find all items for Account with id and count of their reviews
	 * @param id
	 * 		  The Account id that searched.
	 * @param pageable
	 * 		  The pageable object that selects page and sorts.
	 * @return the items that matched the query
	 */
	@Query(value = "SELECT i.item_id AS id, i.item_title AS title, i.item_rating as rating, COUNT(r.review_id) AS reviews FROM items i LEFT JOIN reviews r ON r.review_item = i.item_id WHERE i.item_account = ?1 GROUP BY i.item_id", nativeQuery = true)
	List<ItemWithReviews> findAllForAccountWithReviewCount(int id, Pageable pageable);

	/**
	 * Query to find all items for Account with id and title. Returns items with the count of their reviews
	 * @param id
	 * 		  The Account id that searched.
	 * @param pageable
	 * 		  The pageable object that selects page and sorts.
	 * @return the items that matched the query
	 */
	@Query(value = "SELECT i.item_id AS id, i.item_title AS title, i.item_rating as rating, COUNT(r.review_id) AS reviews FROM items i LEFT JOIN reviews r ON r.review_item = i.item_id WHERE i.item_title LIKE ?1 AND i.item_account = ?2 GROUP BY i.item_id", nativeQuery = true)
	List<ItemWithReviews> findAllForAccountWithReviewCountWithTitle(String title, int id, Pageable pageable);

	/**
	 * Query to find count of all items for Account.
	 * @param id
	 * 		  The Account id that searched.
	 * @return the count of items that matched the query
	 */
	@Query(value = "SELECT COUNT(*) FROM items i WHERE i.item_account = ?1", nativeQuery = true)
    int findItemCountForAccountId(int id);

	/**
	 * Query to find average of ratings for Accounts items
	 * @param id
	 * 		  The Account id that searched.
	 * @return the avg of matched items
	 */
	@Query(value = "SELECT AVG(item_rating) FROM items i WHERE i.item_account = ?1", nativeQuery = true)
	Optional<Float> findItemAvgRatingForAccountId(int id);
}
