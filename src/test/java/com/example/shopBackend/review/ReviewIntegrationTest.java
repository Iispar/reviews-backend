package com.example.shopBackend.review;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReviewIntegrationTest {
    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ReviewRepository reviewRepository;

    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("review-api.baseUrl",
                () -> mockWebServer.url("/").toString());
    }

    @Test
    void addReviewWorks() {
        int reviews = reviewRepository.findAll().size();

        mockWebServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .setBody(
                                """
                                {
                                  "reviews": [
                                    {
                                      "review": "this sucks!! and is the worst item ever",
                                      "star": 1
                                    }
                                  ],
                                  "topNeg": [
                                    "not enough words"
                                  ],
                                  "topPos": [
                                    "not enough words"
                                  ]
                                }
                                """
                        )
        );

        webClient.post().uri("/api/review/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                        """
                        [{
                            "title": "test",
                            "body": "this sucks!! and is the worst item ever",
                            "date": "2020-03-15",
                            "rating": 5,
                            "likes": 0,
                            "dislikes": 0,
                            "account": {
                                "id": 1
                                },
                            "item": {
                                "id": 1
                            }
                        }]
                                                
                        """
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("test")
                .jsonPath("$[0].body").isEqualTo("this sucks!! and is the worst item ever")
                .jsonPath("$[0].date").isEqualTo("2020-03-15")
                .jsonPath("$[0].rating").isEqualTo(1)
                .jsonPath("$[0].likes").isEqualTo(0)
                .jsonPath("$[0].dislikes").isEqualTo(0)
                .jsonPath("$[0].account.id").isEqualTo(1)
                .jsonPath("$[0].item.id").isEqualTo(1);

        assertEquals(reviewRepository.findAll().size(), reviews + 1);
    }

    @Test
    void getReviewsForAccount() {
        webClient.get().uri("/api/review/get/account?accountId=1&page=0")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("title")
                .jsonPath("$[0].body").isEqualTo("body")
                .jsonPath("$[0].date").isEqualTo("2022-02-02")
                .jsonPath("$[0].rating").isEqualTo(4)
                .jsonPath("$[0].likes").isEqualTo(2)
                .jsonPath("$[0].dislikes").isEqualTo(2)
                .jsonPath("$[0].account.id").isEqualTo(1)
                .jsonPath("$[0].item.id").isEqualTo(1);

    }

    @Test
    void getReviewsForItem() {
        webClient.get().uri("/api/review/get/item?itemId=1&page=0&sort=review_date&sortDir=asc")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("title")
                .jsonPath("$[0].body").isEqualTo("body")
                .jsonPath("$[0].date").isEqualTo("2022-02-02")
                .jsonPath("$[0].rating").isEqualTo(4)
                .jsonPath("$[0].likes").isEqualTo(2)
                .jsonPath("$[0].dislikes").isEqualTo(2)
                .jsonPath("$[0].account.id").isEqualTo(1)
                .jsonPath("$[0].item.id").isEqualTo(1);

    }

    @Test
    void getReviewsForTitleSearch() {
        webClient.get().uri("/api/review/get/search/title?title=tit&itemId=1&page=0&sort=review_date&sortDir=asc")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("title")
                .jsonPath("$[0].body").isEqualTo("body")
                .jsonPath("$[0].date").isEqualTo("2022-02-02")
                .jsonPath("$[0].rating").isEqualTo(4)
                .jsonPath("$[0].likes").isEqualTo(2)
                .jsonPath("$[0].dislikes").isEqualTo(2)
                .jsonPath("$[0].account.id").isEqualTo(1)
                .jsonPath("$[0].item.id").isEqualTo(1);

    }

    @Test
    void getReviewsForBodySearch() {
        webClient.get().uri("/api/review/get/search/body?body=bo&itemId=1&page=0&sort=review_date&sortDir=asc")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("title")
                .jsonPath("$[0].body").isEqualTo("body")
                .jsonPath("$[0].date").isEqualTo("2022-02-02")
                .jsonPath("$[0].rating").isEqualTo(4)
                .jsonPath("$[0].likes").isEqualTo(2)
                .jsonPath("$[0].dislikes").isEqualTo(2)
                .jsonPath("$[0].account.id").isEqualTo(1)
                .jsonPath("$[0].item.id").isEqualTo(1);

    }

    @Test
    void getChartForAccount() {
        webClient.get().uri("/api/review/get/chart/account?accountId=1&time=week")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].count").isEqualTo(1);

    }

    @Test
    void getChartForItem() {
        webClient.get().uri("/api/review/get/chart/item?itemId=1&time=week")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].count").isEqualTo(1);

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void deleteReviewWorks() {
        webClient.delete().uri("/api/review/del?reviewId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody().toString().equals("true");

        assertEquals(0, reviewRepository.findAll().size());

    }
}