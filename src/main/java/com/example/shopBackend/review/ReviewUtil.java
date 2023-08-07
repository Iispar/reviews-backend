package com.example.shopBackend.review;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;




/**
 * Rates the given reviews with a call to the API.
 */
@Service
public class ReviewUtil {
    @Value("${review-api.baseUrl}")
    String baseUrl;
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
