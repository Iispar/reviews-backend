package com.example.shopBackend.security;

import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ReturnableItem;
import com.example.shopBackend.review.ReturnableReview;
import com.example.shopBackend.review.Review;

public class Converters {
    public static ReturnableReview convertToReturnableReview(Review input) {
        ReturnableReview returnableReview = new ReturnableReview();
        returnableReview.setDate(input.getDate());
        returnableReview.setBody(input.getBody());
        returnableReview.setTitle(input.getTitle());
        if (input.getLikes() != null) returnableReview.setLikes(input.getLikes());
        else  returnableReview.setLikes(0);
        if (input.getDislikes() != null) returnableReview.setDislikes(input.getDislikes());
        else returnableReview.setDislikes(0);
        returnableReview.setAccountId(input.getAccount().getId());
        returnableReview.setItemId(input.getItem().getId());
        returnableReview.setRating(input.getRating());
        return returnableReview;
    }
    public static ReturnableItem convertToReturnableItem(Item input) {
        ReturnableItem returnableItem = new ReturnableItem();
        returnableItem.setId(input.getId());
        returnableItem.setTitle(input.getTitle());
        returnableItem.setAccountId(input.getAccount().getId());
        returnableItem.setRating(input.getRating());
        returnableItem.setCategoryId(input.getCategory().getId());
        return returnableItem;
    }
}
