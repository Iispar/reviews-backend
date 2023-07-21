package com.example.shopBackend.item;

import com.example.shopBackend.category.CategoryRepository;
import com.example.shopBackend.user.UserRepository;
import com.example.shopBackend.words.Words;
import com.example.shopBackend.words.WordsRepository;
import exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	CategoryRepository categoryRepository;

	@Autowired
	private WordsRepository wordsRepository;

	public ItemService(ItemRepository itemRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
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
	 * @return reviews that match query.
	 */
	public List<Item> getItemsForUser(int id, int page) {
		if(userRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No users exists with id " + id);
		}

		Pageable pageRequest = PageRequest.of(page, 6);
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
					"No items exists with id " + id);
		}

		foundItem.setCategory(item.getCategory());
		foundItem.setTitle(item.getTitle());
		foundItem.setDesc(item.getDesc());

		return itemRepository.save(foundItem);
	}
}
