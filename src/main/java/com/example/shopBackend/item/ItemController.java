package com.example.shopBackend.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for calls to item table.
 * @author iiro
 *
 */
@RestController
@RequestMapping("/api/item")
public class ItemController {
	private ItemService itemService;
	
	@Autowired
	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}
	
	/**
	 * API GET call to /api/item/add with content in the body that describes the added item.
	 * Will add it to the database. Used in the frontend allItem page with add item.
	 * @param {Item} item
	 * 	      The item to be added to the database
	 * @return True if successful. False otherwise
	 */
	@PostMapping("/add")
	public Boolean add(@RequestBody List<Item> review) {
		// TODO: calc average for item
		// TODO: calc topwords
		itemService.saveReview(review);
		return true;
	}
	
	/**
	 * API DELETE call to /api/item/del?itemId=(input)
	 * will delete the item with the corresponding id.
	 * @return True if successful. False otherwise
	 */
	@DeleteMapping("/del")
	public boolean deleteItem(@RequestParam("itemId") int id) {
		if (Boolean.TRUE.equals(itemService.deleteReview(id))) return true;
		return false;
	}
}
