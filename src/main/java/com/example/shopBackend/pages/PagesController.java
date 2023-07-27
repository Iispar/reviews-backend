package com.example.shopBackend.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param {ind} id
     *        The id we want the home page data for
     * @return homepage data for user.
     */
    @GetMapping("/get/home")
    public Homepage getHomePageForUser(
        @RequestParam("userId") int id) {
        return pagesService.getHomepageForUser(id);
    }

    // @GetMapping("/get/item")
    // TODO: CREATE CALL TO SERVICE
    // TODO: CREATE SERVICE TO CALL ALL THE DATA THAT WE NEED
    // TODO: HOW TO RETURN? WHAT TYPE OF OBJECT?
}
