package com.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
public class LocalResponseService {


    @Data
    @RequiredArgsConstructor
    public static class Response {

        private final boolean result;
        private final long delayInMillis;

    }

    public Mono<Response> nonBlocking(long delay) {
        return response(delay)
                .delayElement(Duration.ofMillis(delay));
    }

    public Mono<Response> blocking(long delay) {
        return response(delay)
                .doOnNext(response -> block(delay));
    }

    private void block(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Response blockingLegacy(long delay) {
        block(delay);
        return new Response(true, delay);
    }

    private Mono<Response> response(long delay) {
        return Mono.just(new Response(true, delay));
    }

}
