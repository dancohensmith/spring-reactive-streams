package com.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
@RequestMapping("/controller")
public class WebfluxController {

    private final WebClient client;
    @Data
    @RequiredArgsConstructor
    public static class Response {
        private final boolean success;
        private final long delayInMillis;
    }

    @GetMapping(value = "/blocking/{delay}")
    public Mono<Response> blocking(@PathVariable("delay") long delay) {
        return Mono.just(new Response(true, delay)).delayElement(Duration.ofMillis(delay))
                .doOnNext(response -> {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @GetMapping(value = "/nonBlocking/{delay}")
    public Mono<Response> nonBlocking(@PathVariable("delay") long delay) {
        return Mono.just(new Response(true, delay))
                .delayElement(Duration.ofMillis(delay));
    }

    // This is pretty much the same as the /blocking/{delay} so perhaps we should replace it and have only one?
    @GetMapping(value = "/legacyBlocking/{delay}")
    public Mono<Response> legacyBlocking(@PathVariable("delay") long delay) {
       return Mono.fromCallable(() -> {
           try {
               Thread.sleep(delay);
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
           return new Response(true, delay);
       }).subscribeOn(Schedulers.elastic());
    }


    @GetMapping(value = "/ui/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<String> getUsers() {
        return client
                .get()
                .uri("/api/users")
                .retrieve()
                .bodyToMono(String.class);
    }
}
