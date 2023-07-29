package com.example.shopBackend.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repo for the Categories.
 * @author iiro
 *
 */
@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>, JpaRepository<Category, Integer> {

}
