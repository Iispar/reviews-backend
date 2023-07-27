package com.example.shopBackend.pages;

import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.review.Chart;
import com.example.shopBackend.review.Review;
import com.example.shopBackend.review.ReviewRepository;
import com.example.shopBackend.user.UserRepository;
import com.example.shopBackend.words.Words;
import exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagesService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    public PagesService(ReviewRepository reviewRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates an Homepage object with calls to the item and review repositories.
     * @param {int} userId
     *        The id of the user for homepage.
     * @return Homepage of user with param id.
     */
    public Homepage getHomepageForUser(int userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new BadRequestException(
                    "no users with id: " + userId + " exists");
        }

        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());
        Pageable itemPageReq = PageRequest.of(0, 4, Sort.by("item_rating").ascending());

        int reviewCount = reviewRepository.findCountWithUserId(userId);
        int itemCount = itemRepository.findItemCountForUserId(userId);

        List<Review> latestReviews = reviewRepository.findAllUserId(userId, reviewPageReq);
        List<Item> topItems = itemRepository.findAllUserId(userId, itemPageReq);
        List<Chart> chart = reviewRepository.findChartForUserByWeek(userId);

        float ratingAvg = itemRepository.findItemAvgRatingForUserId(userId).orElse(0F);

        return new Homepage(latestReviews, topItems, ratingAvg, itemCount, reviewCount, chart);
    }

    /**
     * Creates an Itempage object with calls to the item and review repositories.
     * @param {int} itemId
     *        The id of the item for itempge.
     * @return itempage of item with param id.
     */
    public ItemPage getItempageForItem(int itemId) {
        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());

        Item item = itemRepository.findById(itemId).orElse(null);

        if (item == null) {
            throw new BadRequestException(
                    "no items with id: " + itemId + " exists"
            );
        }

        List<Review> latestReviews = reviewRepository.findAllItemId(itemId, reviewPageReq);

        List<Chart> chart = reviewRepository.findChartForItemByWeek(itemId);
        Words words = item.getWords();

        return new ItemPage(latestReviews, chart, words.getPositive(), words.getNegative());
    }
}
