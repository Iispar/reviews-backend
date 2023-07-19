package com.example.shopBackend.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.user.UserRepository;

import exception.BadRequestException;

/**
 * Services for the review entity
 * @author iiro
 *
 */
@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ItemRepository itemRepository) {
		this.reviewRepository = reviewRepository;
		this.userRepository = userRepository;
		this.itemRepository = itemRepository;
	}

	/**
	 * Saves a new review to the database.
	 * @param {Review} review
	 * 		  The review to be added to the database.
	 * @return
	 */
	public List<Review> saveAllReviews(List<Review> review) {
		return reviewRepository.saveAll(review);
	}
	
	/**
	 * Finds all reviews for users items for page from the database. And returns them.
	 * @param {int} id
	 * 		  The id of the user you want reviews for.
	 * @param {int} page
	 * 		  The page you want to receive
	 * @return reviews that match query.
	 */
	public List<Review> getReviewsForUser(int id, int page) {
		Pageable pageRequest = PageRequest.of(page, 4);
		if(userRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No users exists with id " + id);
		}
		// if no user with id return error?
		return reviewRepository.findAllUserId(id, pageRequest);
	}
	
	/**
	 * Finds all reviews for item page from the database. And returns them.
	 * @param {int} id
	 * 		  The id of the item you want reviews for.
	 * @param {int} page
	 * 		  The page you want to receive
	 * @return reviews that match query.
	 */
	public List<Review> getReviewsForItem(int id, int page, String sort, String sortDir) {
		if(itemRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No items exists with id " + id);
		}
		
		if (!(sortDir.equals("asc") || sortDir.equals("desc"))) {
			throw new BadRequestException(
					"sort direction " + sortDir + " is not supported. Has to be either asc or desc.");
		}
		
		if (!(sort.equals("review_date") || sort.equals("review_likes") || sort.equals("review_dislikes") || sort.equals("review_likes") || sort.equals("review_rating"))) {
			throw new BadRequestException(
					"sort " + sort + " is not a valid value for a sort in the entity.");
		}
		
		Pageable pageRequest;
		if (sortDir == "asc") pageRequest = PageRequest.of(page, 4, Sort.by(sort).ascending());
		else pageRequest = PageRequest.of(page, 4, Sort.by(sort).descending());
		
		
		return reviewRepository.findAllItemId(id, pageRequest);
	}
	
	/**
	 * Deletes a item with the corresponding item_id.
	 * @param {int} id
	 * 		  The id of the item to be deleted.
	 * @return true if successful, false otherwise.
	 */
	public Boolean deleteReview(int id) {
		reviewRepository.deleteById(id);
		// false if fails?
		return true;
	}
	
	/**
	 * Finds all reviews for item with title from the database and returns them.
	 * @param {String} title
	 * 		  The searched title.
	 * @param {int} id
	 * 		  The id of the item 
	 * @param {String} sort
	 * 		  The sort used for search
	 * @param {int} page
	 * 		  The page you want to receive
	 * @return reviews that match query.
	 */
	public List<Review> getReviewsWithTitleForItem(String title, int id, int page, String sort, String sortDir) {
		if(itemRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No items exists with id " + id);
		}
		
		if (!(sortDir.equals("asc") || sortDir.equals("desc"))) {
			throw new BadRequestException(
					"sort direction " + sortDir + " is not supported. Has to be either asc or desc.");
		}
		
		if (!(sort.equals("review_date") || sort.equals("review_likes") || sort.equals("review_dislikes") || sort.equals("review_likes") || sort.equals("review_rating"))) {
			throw new BadRequestException(
					"sort " + sort + " is not a valid value for a sort in the entity.");
		}
		
		Pageable pageRequest;
		if (sortDir == "asc") pageRequest = PageRequest.of(page, 4, Sort.by(sort).ascending());
		else pageRequest = PageRequest.of(page, 4, Sort.by(sort).descending());
		
		String formattedTitle = String.format("%%%s%%", title).replaceAll("[ ,_]", "%");
		
		return reviewRepository.findAllByTitleForItem(formattedTitle, id, pageRequest);
	}
	
	/**
	 * Finds the count of reviews and their average rating by user id and returns them
	 * Different query depending on the wanted grouping.
	 * @param {int} id
	 * 	      Id of the user you wish to get results for.
	 * @param {string} time
	 * 		  Either month or week, the selection for grouping of results.
	 * @return count of reviews and their avg rating grouped by parameter.
	 */
	public List<Chart> getChartForUser(String time, int id) {
		
		if (!(time.equals("month") || time.equals("week"))) {
			throw new BadRequestException(
					"time " + time + " is not a valid value for a timespan . Either week or month");
		}
		
		if(userRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No users exists with id " + id);
		}

		List<Chart> res = null;
		if (time .equals("month")) {
			res =  reviewRepository.findChartForUserByMonth(id);
		} else if (time.equals("week")) {
			res =  reviewRepository.findChartForUserByWeek(id);
		}
		return res;
	}
	
	/**
	 * Finds the count of reviews and their average rating by item id and returns them
	 * Different query depending on the wanted grouping.
	 * @param {int} id
	 * 	      Id of the item you wish to get results for.
	 * @param {string} time
	 * 		  Either month or week, the selection for grouping of results.
	 * @return count of reviews and their avg rating grouped by parameter.
	 */
	public List<Chart> getChartForItem(String time, int id) {
		
		if (!(time.equals("month") || time.equals("week"))) {
			throw new BadRequestException(
					"time " + time + " is not a valid value for a timespan . Either week or month");
		}
		
		if(itemRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No items exists with id " + id);
		}
		
		List<Chart> res = null;
		if (time .equals("month")) {
			res =  reviewRepository.findChartForItemByMonth(id);
		} else if (time.equals("week")) {
			res =  reviewRepository.findChartForItemByWeek(id);
		}
		return res;
	}
}
