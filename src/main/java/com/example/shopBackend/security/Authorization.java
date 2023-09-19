package com.example.shopBackend.security;

import com.example.shopBackend.account.Account;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.review.Review;
import com.example.shopBackend.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Methods used to check authorization to API calls.
 */
@Component("authorization")
public class Authorization {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ReviewRepository reviewRepository;

    /**
     * Checks that the inputted id of item is the authenticated users item.
     * @param authentication
     *        Authenticated accounts authentication.
     * @param id
     *        id of item that is being accessed.
     * @return true if own item false otherwise.
     */
    public boolean isOwnItem(Authentication authentication, int id) {
        Account account = (Account) authentication.getPrincipal();
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) return false;
        return account.getId() == item.getAccount().getId();
    }

    /**
     * Checks that the inputted id of review is the authenticated accounts review.
     * @param authentication
     *        Authenticated accounts authentication.
     * @param id
     *        id of review that is being accessed.
     * @return true if own review false otherwise.
     */
    public boolean isOwnReview(Authentication authentication, int id) {
        Account account = (Account) authentication.getPrincipal();
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return false;
        return account.getId() == review.getAccount().getId();
    }

    /**
     * Checks that all items that are being added are authenticated accounts items
     * @param authentication
     *        Authenticated accounts authentication.
     * @param items
     *        items being added to database.
     * @return true if own items false otherwise.
     */
    public boolean addItemsAreOwn(Authentication authentication, List<Item> items) {
        Account account = (Account) authentication.getPrincipal();
        boolean res = true;
        for (Item item : items)
            if (item.getAccount().getId() != account.getId()) {
                res = false;
                break;
            }

        return res;
    }

    /**
     * Checks that all reviews that are being added are authenticated accounts reviews
     * @param authentication
     *        Authenticated accounts authentication.
     * @param reviews
     *        reviews being added to database.
     * @return true if own reviews false otherwise.
     */
    public boolean addReviewsAreOwn(Authentication authentication, List<Review> reviews) {
        Account account = (Account) authentication.getPrincipal();
        boolean res = true;
        for (Review review : reviews)
            if (review.getAccount().getId() != account.getId()) {
                res = false;
                break;
            }

        return res;
    }
}