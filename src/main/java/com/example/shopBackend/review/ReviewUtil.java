package com.example.shopBackend.review;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;




/**
 * Utils for reviews
 */
@Service
public class ReviewUtil {

    // get url from properties
    @Value("${review-api.baseUrl}")
    String baseUrl;

    /**
     * Rates the reviews by calling the rating API.
     * @param reviews
     *        Reviews to be rated.
     * @return rated reviews
     */
    public RatedReviews rateReviews(List<String> reviews) {

        JSONObject jsonReviews = new JSONObject();
        jsonReviews.put("reviews", reviews);

        WebClient webClient = WebClient.create(baseUrl);

        return webClient.post()
                .uri("/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonReviews)
                .retrieve()
                .bodyToMono(RatedReviews.class)
                .block();
    }
}
