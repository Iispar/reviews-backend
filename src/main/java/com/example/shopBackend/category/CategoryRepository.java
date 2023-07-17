package com.example.shopBackend.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.shopBackend.user.User;

/**
 * Repo for the Categories.
 * @author iiro
 *
 */
@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>, JpaRepository<Category, Integer> {
	
	/**
	 * Override findById to return only one Category.
	 * @param {int} id
	 * 		  id to be used for query
	 * @return one Category category.
	 */
	@Query(value = "SELECT * FROM item_categories c WHERE c.category_id = ?1", nativeQuery = true)
	Category findById(int id);
}
