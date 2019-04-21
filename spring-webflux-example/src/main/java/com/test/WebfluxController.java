package com.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
@RequestMapping("/controller")
public class WebfluxController {

    @Data
    @RequiredArgsConstructor
    private static class Response {

        private final boolean result;
        private final long delayInMillis;

    }

    @GetMapping(value = "/blocking/{delay}")
    @ResponseBody
    public Mono<Response> blocking(@PathVariable("delay") int delay) {
        return Mono.just(new Response(true, (long) delay))
                .doOnNext(response -> {
                    try {
                        Thread.sleep((long) delay);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @GetMapping(value = "/nonBlocking/{delay}")
    @ResponseBody
    public Mono<Response> nonBlocking(@PathVariable("delay") int delay) {
        return Mono.just(new Response(true, (long) delay))
                .delayElement(Duration.ofMillis((long) delay));
    }

    @GetMapping(value = "/legacyBlocking/{delay}")
    @ResponseBody
    public Mono<Response> legacyBlocking(@PathVariable("delay") int delay) {
       return Mono.fromCallable(() -> {
           try {
               Thread.sleep((long) delay);
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
           return new Response(true, (long) delay);
       }).subscribeOn(Schedulers.elastic());

    }

    @GetMapping(value = "/ui/users")
    @ResponseBody
    public Mono<String> getUsers() {
        WebClient client = WebClient.create("http://localhost:8081");
        return client
                .get()
                .uri("/api/users")
                .retrieve()
                .bodyToMono(String.class);
    }
}
