package com.example.shopBackend.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller for calls to item table.
 * @author iiro
 *
 */
@RestController
@RequestMapping("/api/item")
public class ItemController {
	private final ItemService itemService;
	
	@Autowired
	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}
	
	/**
	 * API GET call to /api/item/add with content in the body that describes the added item.
	 * Will add it to the database. Used in the frontend allItem page with add item.
	 *
	 * @param item
	 * 		  The item to be added to the database
	 * @return saved items
	 */
	@PostMapping("/add")
	public List<Item> add(@RequestBody List<Item> item) {
		return itemService.saveAllItems(item);
	}
	
	/**
	 * API GET call to /api/item/get?userId=(input)&page=(input)
	 * will return all items for user on page that is selected.
	 * @param id
	 * 	      The if of the user to get items for
	 * @param page
	 * 	      the page we want for the items
	 * @return requested items
	 */
	@GetMapping("/get")
	public List<Item> getItemsForUser(
			@RequestParam("userId") int id,
			@RequestParam("page") int page) {
		List<Item> items = itemService.getItemsForUser(id, page, "none", "none");
		if (items.isEmpty()) {
			throw new IllegalStateException(
					"found no reviews with user id");
		}
		return items;
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleIllegalState(IllegalStateException illegalStateException) {
		return illegalStateException.getMessage();
	}
	
	/**
	 * API DELETE call to /api/item/del?itemId=(input)
	 * will delete the item with the corresponding id.
	 * @param id
	 * 		  The id of the item we want to delete
	 * @return True if successful. False otherwise
	 */
	@DeleteMapping("/del")
	public boolean deleteItem(@RequestParam("itemId") int id) {
		return Boolean.TRUE.equals(itemService.deleteItem(id));
	}

	/**
	 * API PUT call to /api/item/update?itemId=(input) with an item in the body
	 * will update the corresponding item with the id.
	 * @param id
	 * 		  The id of the item to be updated.
	 * @param item
	 * 		  The item that has updated values.
	 * @return Updated item
	 */
	@PutMapping("/update")
	public Item updateItem(
			@RequestParam("itemId") int id,
			@RequestBody Item item) {
		return itemService.updateItem(id, item);
	}
}
