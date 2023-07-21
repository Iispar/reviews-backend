package com.example.shopBackend.item;

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
	private WordsRepository wordsRepository;

	public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Saves a new item to the database.
	 * @param {Item} item
	 * 		  The item to be added to the database.
	 * @return
	 */
	public List<Item> saveItem(List<Item> item) {
		for (int i = 0; i < item.size(); i += 1) {
			Words words = new Words();
			wordsRepository.save(words);
			item.get(i).setWords(words);
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
