package com.example.shopBackend.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
