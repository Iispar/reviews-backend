package com.example.shopBackend.review;

import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class ReviewUtil {
    public static void rateReviews(List<String> reviews) {
        JSONObject jsonReviews = new JSONObject();
        jsonReviews.put("reviews", reviews);


        WebClient client = WebClient.create("http://127.0.0.1:8000");

        RatedReviews ratedReviews = client.post()
                .uri("/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonReviews)
                .retrieve()
                .bodyToMono(RatedReviews.class)
                .block();
        System.out.println(ratedReviews.getReviews().get(0).getReview());
    }
}
