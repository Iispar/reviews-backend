package com.example.shopBackend.item;

import com.example.shopBackend.category.CategoryRepository;
import com.example.shopBackend.review.ReviewRepository;
import com.example.shopBackend.user.UserRepository;
import com.example.shopBackend.words.Words;
import com.example.shopBackend.words.WordsRepository;
import exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Services for the item table.
 * @author iiro
 *
 */
@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	private WordsRepository wordsRepository;

	public ItemService(ItemRepository itemRepository, UserRepository userRepository, CategoryRepository categoryRepository, WordsRepository wordsRepository) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.wordsRepository = wordsRepository;
	}

	/**
	 * Saves a new item to the database.
	 * @param {Item} item
	 * 		  The item to be added to the database.
	 * @return
	 */
	public List<Item> saveAllItems(List<Item> item) {
		for (int i = 0; i < item.size(); i += 1) {
			Words words = new Words();
			wordsRepository.save(words);
			item.get(i).setWords(words);

			if (item.get(i).getRating() < 0 || item.get(i).getRating() > 5) {
				throw new BadRequestException(
						"item with invalid rating. Has to be between 0-5.");
			}

			if (item.get(i).getTitle().length() < 3 || item.get(i).getTitle().length() > 50) {
				throw new BadRequestException(
						"item with invalid title. Length has to be between 3 and 50 characters");
			}
			if (item.get(i).getDesc().length() > 300) {
				throw new BadRequestException(
						"item with invalid desc. Length has to be under 300 characters");
			}

			int categoryId = item.get(i).getCategory().getId();
			int userId = item.get(i).getUser().getId();

			if (categoryRepository.findById(categoryId).isEmpty()) {
				throw new BadRequestException(
						"category with id: " + categoryId + " does not exist");
			}

			if (userRepository.findById(userId).isEmpty()) {
				throw new BadRequestException(
						"user with id: " + userId + " does not exist");
			}
		}
		return itemRepository.saveAll(item);
	}
	/**
	 * Finds all items for user. And returns them.
	 * @param {int} id
	 * 		  The id of the user you want items for.
	 * @param {int} page
	 * 		  The page you want to receive
	 * @param {String} sort
	 * 		  Entity value to be sorted with, none if no sort.
	 * @param {String} sortDir
	 * 		  The direction of the sort, none if no sort.
	 * @return reviews that match query.
	 */
	public List<Item> getItemsForUser(int id, int page, String sort, String sortDir) {

		if (!(sortDir.equals("asc") || sortDir.equals("desc") || sortDir.equals("none"))) {
			throw new BadRequestException(
					"sort direction " + sortDir + " is not supported. Has to be either asc or desc.");
		}

		if (!(sort.equals("item_rating") || sort.equals("none"))) {
			throw new BadRequestException(
					"sort " + sort + " is not a valid value for a sort in the entity.");
		}

		if(userRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No users exists with id " + id);
		}

		PageRequest pageRequest;
		if (sortDir.equals("none")) pageRequest = PageRequest.of(page, 6);
		else if (sortDir.equals("asc")) pageRequest = PageRequest.of(page, 6, Sort.by(sort).ascending());
		else pageRequest = PageRequest.of(page, 6, Sort.by(sort).descending());

		return itemRepository.findAllUserId(id, pageRequest);
	}
	
	/**
	 * deletes a item from the database.
	 * @param {id} item
	 * 		  The item to be deleted from the database.
	 * @return
	 */
	public Boolean deleteItem(int id){
		if(itemRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No items exists with id " + id);
		}

		itemRepository.deleteById(id);
		// check if fails
		return true;
	}

	/**
	 * Sets the new values to the updated item and saves it to the repository,
	 * @param {int} id
	 * 		  Id of the item to be updated.
	 * @param {Item} item
	 * 		  The item that has updated values.
	 * @return Updated item
	 */
	public Item updateItem(int id, Item item) {
		Item foundItem = itemRepository.findById(id).orElse(null);
		if (foundItem == null) {
			throw new BadRequestException(
					"No items exists with id: " + id);
		}

		if (categoryRepository.findById(item.getCategory().getId()).isEmpty()) {
			throw new BadRequestException(
					"No categories exists with id: " + item.getCategory().getId());
		}

		if (item.getTitle().length() < 3 || item.getTitle().length() > 50) {
			throw new BadRequestException(
					"item with invalid title. Length has to be between 3 and 50 characters");
		}

		if (item.getDesc().length() > 300) {
			throw new BadRequestException(
					"item with invalid desc. Length has to be under 300 characters");
		}

		foundItem.setCategory(item.getCategory());
		foundItem.setTitle(item.getTitle());
		foundItem.setDesc(item.getDesc());

		return itemRepository.save(foundItem);
	}

	/**
	 * updates the items rating and words.
	 * @param {int} id
	 * 		  The id of the item you would like to update
	 * @param {List<String>} posWords
	 * 		  The pos words to be inserted to positive words.
	 * @param {List<String>} negWords
	 *  	  The neg words to be inserted to negative words.
	 * @return saved updated item
	 */
	public Item updateItemRatingAndWords(int id, List<String> posWords, List<String> negWords) {
		if (itemRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"no items with id: " + id + " exists");
		}

		Item foundItem = itemRepository.findById(id).orElseThrow();
		List<Integer> ratings = reviewRepository.findAllRatingsWithItemId(id);

		int rating = ratings.stream().mapToInt(Integer::intValue).sum()/ratings.size();
		if (rating < 1 || rating > 5) {
			throw new BadRequestException(
					"item rating invalid. Allowed only 1-5."
			);
		}

		if (posWords.size() > 5 || posWords.size() < 1) {
			throw new BadRequestException(
					"pos words invalid. Has to have 1 to 5 items."
			);
		}

		if (negWords.size() > 5 || negWords.size() < 1) {
			throw new BadRequestException(
					"neg words invalid. Has to have 1 to 5 items."
			);
		}

		foundItem.setRating(rating);
		foundItem.getWords().setPositive(posWords);
		foundItem.getWords().setNegative(negWords);
		return itemRepository.save(foundItem);
	}
}
