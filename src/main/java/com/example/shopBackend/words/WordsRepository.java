package com.example.shopBackend.words;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repo for the words.
 * @author iiro
 *
 */
@Repository
public interface WordsRepository extends PagingAndSortingRepository<Words, Integer>, JpaRepository<Words, Integer> {
	
	/**
	 * Override findById to return only one Words.
	 * @param {int} id
	 * 		  id to be used for query
	 * @return one Words words.
	 */
	@Query(value = "SELECT * FROM words w WHERE w.words_id = ?1", nativeQuery = true)
	Words findById(int id);
}
