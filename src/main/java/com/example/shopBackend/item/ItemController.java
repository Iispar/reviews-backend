package com.example.shopBackend.item;

import com.example.shopBackend.response.ListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
	 * API POST call to /api/item/add with content in the body that describes the added item
	 * will add it to the database.
	 *
	 * @param item
	 * 		  The item to be added to the database
	 * @return saved items
	 */
	@PreAuthorize("@authorization.addItemsAreOwn(authentication, #item)")
	@PostMapping("/add")
	public List<Item> add(@RequestBody List<Item> item) {
		return itemService.saveAllItems(item);
	}
	
	/**
	 * API GET call to /api/item/get?accountId=(input)&page=(input)
	 * will return all items for Account on page that is selected.
	 * @param id
	 * 	      The if of the Account to get items for
	 * @param page
	 * 	      the page we want for the items
	 * @return requested items
	 */
	@PreAuthorize("#id == authentication.principal.id")
	@GetMapping("/get")
	public ListRes getItemsForAccount(
			@RequestParam("sort") String sort,
			@RequestParam("sortDir") String sortDir,
			@RequestParam("accountId") int id,
			@RequestParam("page") int page) {
		return itemService.getItemsForAccount(id, page, sort, sortDir);
	}

	/**
	 * API GET call to /api/item/get/search?title=(input)&accountId=(input)&page=(input)
	 * will return all items for Account on page that is selected.
	 * @param title
	 * 	      The title to get items for
	 * @param id
	 * 	      The id of the Account to get items for
	 * @param page
	 * 	      the page we want for the items
	 * @param sort
	 * 		  The sort used for search
	 * @param sortDir
	 * 		  The direction of the sort
	 * @return items that match title and search
	 */
	@PreAuthorize("#id == authentication.principal.id")
	@GetMapping("/get/search")
	public ListRes getItemsForAccountWithTitle(
			@RequestParam("title") String title,
			@RequestParam("accountId") int id,
			@RequestParam("sort") String sort,
			@RequestParam("sortDir") String sortDir,
			@RequestParam("page") int page) {
		return itemService.getItemsForAccountWithTitleAndSorts(title, id, page, sort, sortDir);
	}

	// exception handler that is used for tests.
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
	 * @return True if successful. Error otherwise
	 */
	@PreAuthorize("@authorization.isOwnItem(authentication, #id)")
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
	@PreAuthorize("@authorization.isOwnItem(authentication, #id)")
	@PutMapping("/update")
	public Item updateItem(
			@RequestParam("itemId") int id,
			@RequestBody Item item) {
		return itemService.updateItem(id, item);
	}
}
