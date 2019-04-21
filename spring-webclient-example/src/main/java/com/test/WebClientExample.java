package com.test;

import com.test.model.UserRegisteredEvent;
import io.netty.util.concurrent.CompleteFuture;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
public class WebClientExample {
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
                    .doOnNext(userRegisteredEvent -> log.info("Recevied {}", userRegisteredEvent))
                    .count().block();

            log.info("Total received {}", received );
        } catch (Exception e) {
            log.error("Failed to count registrations" , e);
        }

    }
}
