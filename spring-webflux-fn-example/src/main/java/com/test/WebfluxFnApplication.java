package com.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Slf4j
@SpringBootApplication
public class WebfluxFnApplication {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        SpringApplication.run(WebfluxFnApplication.class, args);
    }

    @Bean
    public RouterFunction route() {
        return RouterFunctions
                .route(GET("/userRegistrations").and(accept(APPLICATION_STREAM_JSON)), this::userRegistrationsHandler);
    }

    private Mono<ServerResponse> userRegistrationsHandler(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(APPLICATION_STREAM_JSON)
                .body(userRegistrations(serverRequest), UserRegisteredEvent.class);
    }

    public Flux<UserRegisteredEvent> userRegistrations(ServerRequest serverRequest) {
        final AtomicLong index = new AtomicLong();
        return Flux.<UserRegisteredEvent>create(fluxSink -> {
            log.info("Creating sink...");
            createRegistrations(index, fluxSink);
        }, FluxSink.OverflowStrategy.BUFFER)// DISCUSS OTHER STRATEGIES, IGNORE, ERROR, DROP, LATEST
        .share();
    }

    private void createRegistrations(AtomicLong index, FluxSink<UserRegisteredEvent> fluxSink) {
        log.info("Generating New Batch Of registrations");
        int batchSize = 10000;
        for (int i = 0; i < batchSize; i++) {
            long id = index.incrementAndGet();
            UserRegisteredEvent newUserRegisteredEvent = new UserRegisteredEvent(id, "John Smith " + id);
            log.info("New User Registered: {}", newUserRegisteredEvent);
            fluxSink.next(newUserRegisteredEvent);
        }
        log.info("Generated {}", batchSize);
        //scheduler.schedule(() -> createRegistrations(index, fluxSink), 10, TimeUnit.SECONDS);
    }
}
