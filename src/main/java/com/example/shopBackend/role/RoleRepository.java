package com.example.shopBackend.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repo for the Role table.
 */
@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer>, JpaRepository<Role, Integer> {
}
