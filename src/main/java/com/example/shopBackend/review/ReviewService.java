package com.example.shopBackend.review;

import com.example.shopBackend.account.AccountRepository;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.item.ItemService;
import exception.BadRequestException;
import exception.CalculationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	private AccountRepository accountRepository;
	
	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ReviewUtil reviewUtil;

	@Autowired
	private ItemService itemService;

	
	public ReviewService(ReviewRepository reviewRepository, AccountRepository accountRepository, ItemRepository itemRepository, ReviewUtil reviewUtil) {
		this.reviewRepository = reviewRepository;
		this.accountRepository = accountRepository;
		this.itemRepository = itemRepository;
		this.reviewUtil = reviewUtil;
	}

	/**
	 * Saves a new review to the database.
	 * @param  review
	 * 		  The review to be added to the database.
	 * @return saved reviews.
	 */
	public List<Review> saveAllReviews(List<Review> review) {

		if (review.isEmpty()) {
			throw new BadRequestException(
					"review input cannot be empty");
		}

		int itemId = review.get(0).getItem().getId();

		if (itemRepository.findById(itemId).isEmpty()) {
			throw new BadRequestException(
					"item with id: " + itemId + " does not exist");
		}

		List<String> reviewBodys = new ArrayList<>();

		for (Review value : review) {
			reviewBodys.add(value.getBody());
		}

		// calculates ratings
		RatedReviews ratedReviews;
		try {
			ratedReviews = reviewUtil.rateReviews(reviewBodys);
		} catch (Exception e) {
			throw new CalculationException("error: " + e.getMessage() + ". While calculating reviews");
		}

		// check for errors with reviews
		for (int i = 0; i < review.size(); i += 1) {

			// sets the new calculated rating.
			review.get(i).setRating(ratedReviews.getReviews().get(i).getStar());

			if (review.get(i).getItem().getId() != itemId) {
				throw new BadRequestException(
						"all reviews don't have the same id");
			}

			if (review.get(i).getRating() > 5 || review.get(i).getRating() < 1) {
				throw new BadRequestException(
						"review with invalid rating. Has to be between 0-5.");
			}

			if (review.get(i).getDislikes() < 0) {
				throw new BadRequestException(
						"review with negative dislikes not allowed");
			}
			
			if (review.get(i).getLikes() < 0) {
				throw new BadRequestException(
						"review with negative likes not allowed");
			}

			int accountId = review.get(i).getAccount().getId();

			if (accountRepository.findById(accountId).isEmpty()) {
				throw new BadRequestException(
						"Account with id: " + accountId + " does not exist");
			}
		}

		// save reviews and update item
		List<Review> res = reviewRepository.saveAll(review);
		itemService.updateItemRatingAndWords(itemId, ratedReviews.getTopPos(), ratedReviews.getTopNeg());
		return res;
	}
	
	/**
	 * Deletes an item with the corresponding item_id.
	 * @param id
	 * 		  The id of the item to be deleted.
	 * @return true
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
	 * Finds all reviews for Accounts with id.
	 * @param id
	 * 		  The id of the Account you want reviews for.
	 * @param page
	 * 		  The page you want to receive
	 * @return reviews with corresponding id
	 */
	public List<Review> getReviewsForAccount(int id, int page, String sort, String sortDir) {
		Pageable pageRequest;

		if (!(sortDir.equals("asc") || sortDir.equals("desc"))) {
			throw new BadRequestException(
					"sort direction " + sortDir + " is not supported. Has to be either asc or desc.");
		}

		if (!(sort.equals("review_date") || sort.equals("review_likes") || sort.equals("review_dislikes") ||  sort.equals("review_rating"))) {
			throw new BadRequestException(
					"sort " + sort + " is not a valid value for a sort in the entity.");
		}

		if (sortDir.equals("asc")) pageRequest = PageRequest.of(page, 4, Sort.by(sort).ascending());
		else pageRequest = PageRequest.of(page, 4, Sort.by(sort).descending());

		if(accountRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No Accounts exists with id " + id);
		}
		return reviewRepository.findAllAccountId(id, pageRequest);
	}
	
	/**
	 * Finds all reviews for item with corresponding id
	 * @param id
	 * 		  The id of the item you want reviews for.
	 * @param page
	 * 		  The page you want to receive
	 * @return reviews for item
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
		
		if (!(sort.equals("review_date") || sort.equals("review_dislikes") || sort.equals("review_likes") || sort.equals("review_rating"))) {
			throw new BadRequestException(
					"sort " + sort + " is not a valid value for a sort in the entity.");
		}
		
		Pageable pageRequest;
		if (sortDir.equals("asc")) pageRequest = PageRequest.of(page, 4, Sort.by(sort).ascending());
		else pageRequest = PageRequest.of(page, 4, Sort.by(sort).descending());
		
		
		return reviewRepository.findAllItemId(id, pageRequest);
	}
	
	/**
	 * Finds all reviews for item with title from the database and returns them.
	 * @param title
	 * 		  The searched title.
	 * @param id
	 * 		  The id of the item 
	 * @param sort
	 * 		  The sort used for search
	 * @param page
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
		
		if (!(sort.equals("review_date") || sort.equals("review_dislikes") || sort.equals("review_likes") || sort.equals("review_rating"))) {
			throw new BadRequestException(
					"sort " + sort + " is not a valid value for a sort in the entity.");
		}

		// creates pageRequest
		Pageable pageRequest;
		if (sortDir.equals("asc")) pageRequest = PageRequest.of(page, 4, Sort.by(sort).ascending());
		else pageRequest = PageRequest.of(page, 4, Sort.by(sort).descending());

		// formats title for sql.
		String formattedTitle = String.format("%%%s%%", title).replaceAll("[ ,_]", "%");
		
		return reviewRepository.findAllByTitleForItem(formattedTitle, id, pageRequest);
	}

	/**
	 * Finds all reviews for item with body from the database and returns them.
	 * @param body
	 * 		  The searched body.
	 * @param id
	 * 		  The id of the item
	 * @param sort
	 * 		  The sort used for search
	 * @param page
	 * 		  The page you want to receive
	 * @return reviews that match query.
	 */
	public List<Review> getReviewsWithBodyForItem(String body, int id, int page, String sort, String sortDir) {
		if(itemRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No items exists with id " + id);
		}

		if (!(sortDir.equals("asc") || sortDir.equals("desc"))) {
			throw new BadRequestException(
					"sort direction " + sortDir + " is not supported. Has to be either asc or desc.");
		}

		if (!(sort.equals("review_date") || sort.equals("review_dislikes") || sort.equals("review_likes") || sort.equals("review_rating"))) {
			throw new BadRequestException(
					"sort " + sort + " is not a valid value for a sort in the entity.");
		}

		// creates pageRequest.
		Pageable pageRequest;
		if (sortDir.equals("asc")) pageRequest = PageRequest.of(page, 4, Sort.by(sort).ascending());
		else pageRequest = PageRequest.of(page, 4, Sort.by(sort).descending());

		// formats body for sql.
		String formattedBody = String.format("%%%s%%", body).replaceAll("[ ,_]", "%");

		return reviewRepository.findAllByBodyForItem(formattedBody, id, pageRequest);
	}
	
	/**
	 * Finds the count of reviews and their average rating by Account id and returns them
	 * Different query depending on the wanted grouping.
	 * @param id
	 * 	      id of the Account you wish to get results for.
	 * @param time
	 * 		  Either month or week, the selection for grouping of results.
	 * @return list of count of reviews and their avg rating grouped by parameter.
	 */
	public List<Chart> getChartForAccount(String time, int id) {
		
		if (!(time.equals("month") || time.equals("week"))) {
			throw new BadRequestException(
					"time " + time + " is not a valid value for a timespan . Either week or month");
		}
		
		if(accountRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No Accounts exists with id " + id);
		}

		List<Chart> res;
		if (time .equals("month")) {
			res =  reviewRepository.findChartForAccountByMonth(id);
		} else {
			res =  reviewRepository.findChartForAccountByWeek(id);
		}

		Chart empty = new Chart() {
			@Override
			public double getRating() {
				return -1;
			}

			@Override
			public int getCount() {
				return -1;
			}

			@Override
			public String getTime() {
				return null;
			}

			@Override
			public String getYear() {
				return null;
			}
		};

		Collections.reverse(res);
		while (!Arrays.asList(3, 5, 7, 9).contains(res.size())) {
			res.remove(0);
		}

		res.add(0, empty);
		res.add(res.size(), empty);
		return res;
	}
	
	/**
	 * Finds the count of reviews and their average rating by item id and returns them
	 * Different query depending on the wanted grouping.
	 * @param id
	 * 	      id of the item you wish to get results for.
	 * @param time
	 * 		  Either month or week, the selection for grouping of results.
	 * @return list of count of reviews and their avg rating grouped by parameter.
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
		
		List<Chart> res;
		if (time .equals("month")) {
			res =  reviewRepository.findChartForItemByMonth(id);
		} else {
			res =  reviewRepository.findChartForItemByWeek(id);
		}
		return res;
	}
}
