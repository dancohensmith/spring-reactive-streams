package com.test.webflux;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebfluxFnHandler {

    public Mono<ServerResponse> users(ServerRequest serverRequest) {
        log.info("Request for users");
        return ServerResponse.ok().syncBody(IntStream
                .range(1, 200)
                .mapToObj(i -> new User(i, "John Smith " + i))
                .collect(Collectors.toList()));
    }


}
