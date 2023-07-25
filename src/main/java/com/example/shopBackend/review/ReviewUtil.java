package com.example.shopBackend.review;

import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class ReviewUtil {
    public static RatedReviews rateReviews(List<String> reviews) {
        JSONObject jsonReviews = new JSONObject();
        jsonReviews.put("reviews", reviews);

        WebClient client = WebClient.create("http://127.0.0.1:8000");

        return client.post()
                .uri("/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonReviews)
                .retrieve()
                .bodyToMono(RatedReviews.class)
                .block();
    }
}
