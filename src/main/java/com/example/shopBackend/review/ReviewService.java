package com.example.shopBackend.review;

import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.item.ItemService;
import com.example.shopBackend.user.UserRepository;
import exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

	@Autowired
	private ItemService itemService;
	
	public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ItemRepository itemRepository, ItemService itemService) {
		this.reviewRepository = reviewRepository;
		this.userRepository = userRepository;
		this.itemRepository = itemRepository;
		this.itemService = itemService;
	}

	/**
	 * Saves a new review to the database.
	 * @param {List<Review>} review
	 * 		  The review to be added to the database.
	 * @return
	 */
	public List<Review> saveAllReviews(List<Review> review) {
		int itemId = review.get(0).getItem().getId();

		if (itemRepository.findById(itemId).isEmpty()) {
			throw new BadRequestException(
					"item with id: " + itemId + " does not exist");
		}

		List<String> reviewBodys = reviewRepository.findAllBodysWithItemId(itemId);

		for (int i = 0; i < review.size(); i += 1) {
			reviewBodys.add(review.get(i).getBody());
		}

		RatedReviews ratedReviews;
		try {
			ratedReviews = ReviewUtil.rateReviews(reviewBodys);
		} catch(Exception e) {
			throw new RuntimeException("review rating failed");
		}

		for (int i = 0; i < review.size(); i += 1) {

			// sets the new calculated rating.
			review.get(i).setRating(ratedReviews.getReviews().get(i).getStar());
			if (review.get(i).getItem().getId() != itemId) {
				throw new BadRequestException(
						"all reviews don't have the same id");
			}

			if (review.get(i).getDislikes() < 0) {
				throw new BadRequestException(
						"review with negative dislikes not allowed");
			}
			
			if (review.get(i).getLikes() < 0) {
				throw new BadRequestException(
						"review with negative likes not allowed");
			}

			int userId = review.get(i).getUser().getId();

			if (userRepository.findById(userId).isEmpty()) {
				throw new BadRequestException(
						"user with id: " + userId + " does not exist");
			}
		}


		List<Review> res = reviewRepository.saveAll(review);
		itemService.updateItemRatingAndWords(itemId, ratedReviews.getTopPos(), ratedReviews.getTopNeg());
		return res;
	}
	
	/**
	 * Deletes a item with the corresponding item_id.
	 * @param {int} id
	 * 		  The id of the item to be deleted.
	 * @return true if successful, false otherwise.
	 */
	public Boolean deleteReview(int id) {
		if(reviewRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No reviews exists with id " + id);
		}

		reviewRepository.deleteById(id);
		return true;
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

		List<Chart> res = new ArrayList<Chart>();
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
		
		List<Chart> res = new ArrayList<Chart>();
		if (time .equals("month")) {
			res =  reviewRepository.findChartForItemByMonth(id);
		} else if (time.equals("week")) {
			res =  reviewRepository.findChartForItemByWeek(id);
		}
		return res;
	}
}
