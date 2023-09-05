package com.example.shopBackend.pages;

import com.example.shopBackend.account.Account;
import com.example.shopBackend.account.AccountRepository;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.review.BarChart;
import com.example.shopBackend.review.Chart;
import com.example.shopBackend.review.Review;
import com.example.shopBackend.review.ReviewRepository;
import com.example.shopBackend.words.Words;
import exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Services for page calls.
 */
@Service
public class PagesService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Creates a Homepage object with calls to the item and review repositories.
     * @param accountId
     *        The id of the Account for homepage.
     * @return Homepage of Account with param id.
     */
    public Homepage getHomepageForAccount(int accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() ->
                new BadRequestException("no Accounts with id: " + accountId + " exists")
        );

        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());
        Pageable itemPageReq = PageRequest.of(0, 4, Sort.by("item_rating").descending());

        int reviewCount = reviewRepository.findCountWithAccountId(accountId);
        int itemCount = itemRepository.findItemCountForAccountId(accountId);

        List<Review> latestReviews = reviewRepository.findAllAccountId(accountId, reviewPageReq);
        List<Item> topItems = itemRepository.findAllAccountId(accountId, itemPageReq);
        List<Chart> chart = reviewRepository.findChartForAccountByMonth(accountId);

        // reverse the order
        Collections.reverse(chart);
        //calc average of all reviews counts so that the chart starts from the middle
        int avg = (int)chart.stream().map(Chart::getCount).mapToInt(a -> a).average().orElse(0);

        Chart empty = new Chart() {
            @Override
            public double getRating() {
                return -1;
            }

            @Override
            public int getCount() {
                return avg;
            }

            @Override
            public String getTime() {
                return null;
            }

            @Override
            public String getTimeYear() {
                return null;
            }
        };

        // remove if chart has over 5 objects.
        while (!Arrays.asList(0, 3, 5, 7).contains(chart.size())) {
            chart.remove(0);
        }

        // add one empty one to the end and start to display it correctly.
        chart.add(0, empty);
        chart.add(chart.size(), empty);


        List<BarChart> barChart = reviewRepository.findRatingDistributionWithAccountId(accountId);

        // if has any empty ratings create new object with zero count
        // a bit clumsy atm but TODO: optimize this.
        if (barChart.size() != 5) {
            int initValue = 0;
            for (int i = 1; i <= 5; i += 1) {
                if (barChart.get(initValue).getRating() != i) {
                    int finalCurr = i;
                    barChart.add(new BarChart() {
                        @Override
                        public int getRating() {
                            return finalCurr;
                        }

                        @Override
                        public double getCount() {
                            return 0;
                        }
                    });
                } else {
                    initValue++;
                }
            }
        }

        // sort barChart list
        Collections.sort(barChart);

        float ratingAvg = itemRepository.findItemAvgRatingForAccountId(accountId).orElse(0F);

        return new Homepage(account.getName(), latestReviews, topItems, ratingAvg, itemCount, reviewCount, barChart, chart);
    }

    /**
     * Creates an ItemPage object with calls to the item and review repositories.
     * @param  itemId
     *        The id of the item for ItemPge.
     * @return ItemPage of item with param id.
     */
    public ItemPage getItemPageForItem(int itemId) {

        Pageable reviewPageReq = PageRequest.of(0, 4);

        Item item = itemRepository.findById(itemId).orElse(null);

        if (item == null) {
            throw new BadRequestException(
                    "no items with id: " + itemId + " exists"
            );
        }

        String title = item.getTitle();
        double rating = item.getRating();

        int reviews = reviewRepository.findReviewCountForItem(item.getId());
        int positiveReviews = reviewRepository.findPosReviewCountForItem(item.getId());
        int negativeReviews = reviewRepository.findNegReviewCountForItem(item.getId());

        List<Review> latestReviews = reviewRepository.findAllItemId(itemId, reviewPageReq);

        List<Chart> chart = reviewRepository.findChartForItemByMonth(itemId);
        Collections.reverse(chart);
        //calc average of all reviews counts so that the chart starts from the middle
        int avg = (int)chart.stream().map(Chart::getCount).mapToInt(a -> a).average().orElse(0);

        Chart empty = new Chart() {
            @Override
            public double getRating() {
                return -1;
            }

            @Override
            public int getCount() {
                return avg;
            }

            @Override
            public String getTime() {
                return null;
            }

            @Override
            public String getTimeYear() {
                return null;
            }
        };

        // remove if chart has over 5 objects.
        while (!Arrays.asList(0, 3, 5, 7).contains(chart.size())) {
            chart.remove(0);
        }

        // add one empty one to the end and start to display it correctly.
        chart.add(0, empty);
        chart.add(chart.size(), empty);


        Words words = item.getWords();

        return new ItemPage(title, reviews, positiveReviews, negativeReviews, rating, latestReviews, chart, words.getPositive(), words.getNegative());
    }
}
