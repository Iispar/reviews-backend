package com.example.shopBackend.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.shopBackend.review.Review;

/**
 * Services for the item table.
 * @author iiro
 *
 */
@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;
	
	/**
	 * Saves a new item to the database.
	 * @param {Item} item
	 * 		  The item to be added to the database.
	 * @return
	 */
	public List<Item> saveReview(List<Item> item) {
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
		Pageable pageRequest = PageRequest.of(page, 6);
		return itemRepository.findAllUserId(id, pageRequest);
	}
	
	/**
	 * deletes a item from the database.
	 * @param {id} item
	 * 		  The item to be deleted from the database.
	 * @return
	 */
	public Boolean deleteReview(int id){
		itemRepository.deleteById(id);
		// check if fails
		return true;
	}
}
