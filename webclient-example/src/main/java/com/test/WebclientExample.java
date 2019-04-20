package com.test;

import org.reactivestreams.Subscription;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Hooks;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class WebclientExample {
    public static void main(String[] args) throws InterruptedException {
        WebClient.create("http://localhost:8084")
                .get()
                .uri("/userRegistrations")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .log()
                .flatMapMany(response -> response.bodyToFlux(UserRegisteredEvent.class))
                // back pressure example
                .limitRate(10)
                .delayElements(Duration.ofMillis(50))
                .subscribe(
                        newUser -> System.out.println("New UserRegisteredEvent Registration " + newUser),
                        err -> {
                            System.out.println("Error on UserRegisteredEvent Registration Stream: " + err);
                            System.exit(0);
                        },
                        () -> {
                            System.out.println("UserRegisteredEvent Registration stream stopped!");
                            System.exit(0);
                        });

        TimeUnit.SECONDS.sleep(10000);
    }
}
