package com.example.shopBackend.review;

import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Rates the given reviews with a call to the API.
 */
@Service
public class ReviewUtil {

    public RatedReviews rateReviews(List<String> reviews) {

        JSONObject jsonReviews = new JSONObject();
        jsonReviews.put("reviews", reviews);

        WebClient webClient = WebClient.create("http://127.0.0.1:8000");

        return webClient.post()
                .uri("/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonReviews)
                .retrieve()
                .bodyToMono(RatedReviews.class)
                .block();
    }
}
