package com.example.shopBackend.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for calls to retrieve pages.
 */
@RestController
@RequestMapping("/api/pages")
public class PagesController {

    @Autowired
    private PagesService pagesService;

    @Autowired
    public PagesController(PagesService pagesService) {
        this.pagesService = pagesService;
    }

    /**
     * GET call to /get/home?userId=(input) will return all the data
     * for the homepage, with the data corresponding to the user with the param id.
     * @param id
     *        The id we want the home page data for
     * @return homepage data for user.
     */
    @GetMapping("/get/home")
    public Homepage getHomePageForUser(
        @RequestParam("userId") int id) {
        return pagesService.getHomepageForUser(id);
    }


    /**
     * GET call to /get/item?itemId=(input) will return all the data
     * for the ItemPage, with the data corresponding to the item with the param id.
     * @param id
     *        The id we want the home page data for
     * @return ItemPage data for item.
     */
    @GetMapping("/get/item")
    public ItemPage getItemPageForItem(
            @RequestParam("itemId") int id) {
        return pagesService.getItemPageForItem(id);
    }
}
