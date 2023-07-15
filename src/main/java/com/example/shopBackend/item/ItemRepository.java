package com.example.shopBackend.item;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repo for the item table
 * @author iiro
 *
 */
@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, Integer>, JpaRepository<Item, Integer> {
	
	/**
	 * Override findById to return only one Item obejct
	 * @param {int} id
	 * 		  id to use for query.
	 * @return Found Item.
	 */
	@Query(value = "SELECT * FROM items i WHERE i.item_id = ?1", nativeQuery = true)
	Item findById(int id);
	
	/**
	 * Query to find all items for user for page.
	 * @param {int} id
	 * 		  The user id that searched.
	 * @param {Pageable} pageable
	 * 		  The pageable object that selects page.
	 * @return the items that matched the query
	 */
	@Query(value = "SELECT * FROM items i WHERE i.item_account = ?1", nativeQuery = true)
	List<Item> findAllUserId(int id, Pageable pageable);

}
