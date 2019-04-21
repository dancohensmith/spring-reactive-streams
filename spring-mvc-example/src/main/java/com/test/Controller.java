
package com.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@RestController
@RequestMapping("/mvc")
@RequiredArgsConstructor
public class Controller {

    @Data
    @RequiredArgsConstructor
    private static class Response {

        private final boolean result;
        private final long delayInMillis;

    }

    @GetMapping(value = "/blocking/{delay}")
    public Response blocking(@PathVariable("delay") int delay) {
        return Mono.just(new Response(true, (long) delay))
                .doOnNext(response -> {
                    try {
                        Thread.sleep((long) delay);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).block();
    }

    @GetMapping(value = "/nonBlocking/{delay}")
    public Response nonBlocking(@PathVariable("delay") int delay) {
        return Mono.just(new Response(true, (long) delay))
                .delayElement(Duration.ofMillis((long) delay)).block();
    }

    @GetMapping(value = "/legacyBlocking/{delay}")
    public Response legacyBlocking(@PathVariable("delay") int delay) {
        return Mono.fromCallable(() -> {
            try {
                Thread.sleep((long) delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new Response(true, (long) delay);
        })
                .subscribeOn(Schedulers.elastic()).block();
    }

    @GetMapping(value = "/ui/users")
    public String getUsers() {
        WebClient client = WebClient.create("http://localhost:8081");
        return client
                .get()
                .uri("/api/users")
                .retrieve()
                .bodyToMono(String.class).block();
    }
}