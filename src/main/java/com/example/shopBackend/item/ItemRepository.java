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
	 * Query to find all items for user for page.
	 * @param id
	 * 		  The user id that searched.
	 * @param pageable
	 * 		  The pageable object that selects page and sorts.
	 * @return the items that matched the query
	 */
	@Query(value = "SELECT * FROM items i WHERE i.item_account = ?1", nativeQuery = true)
	List<Item> findAllUserId(int id, Pageable pageable);

	/**
	 * Query to find count of all items for user.
	 * @param id
	 * 		  The user id that searched.
	 * @return the count of items that matched the query
	 */
	@Query(value = "SELECT COUNT(*) FROM items i WHERE i.item_account = ?1", nativeQuery = true)
    int findItemCountForUserId(int id);

	/**
	 * Query to find avg of ratings for users items
	 * @param id
	 * 		  The user id that searched.
	 * @return the avg of matched items
	 */
	@Query(value = "SELECT AVG(item_rating) FROM items i WHERE i.item_account = ?1", nativeQuery = true)
	Optional<Float> findItemAvgRatingForUserId(int id);
}
