package com.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;


@Slf4j
public class WebClientExample {

    @Data
    @RequiredArgsConstructor
    private static class UserRegisteredEvent {
        private final long id;
        private final String name;
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            Long received = WebClient.create("http://localhost:8083")
                    .get()
                    .uri("/fn/userRegistrations")
                    .accept(MediaType.APPLICATION_STREAM_JSON)
                    .exchange()
                    .flatMapMany(response -> response.bodyToFlux(UserRegisteredEvent.class))
                    // back pressure example
                    .limitRate(10)
                    .delayElements(Duration.ofMillis(50))
                    .doOnNext(userRegisteredEvent -> log.info("Received {}", userRegisteredEvent))
                    .count().block();

            log.info("Total received {}", received );
        } catch (Exception e) {
            log.error("Failed to count registrations" , e);
        }

    }
}
