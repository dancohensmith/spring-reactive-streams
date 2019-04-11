package com.test;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

@RestController
public class MvcController {
    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "Hello World!";
    }

    @RequestMapping("/blocking")
    @ResponseBody
    public String blocking() throws InterruptedException {
        int millis = new Random().nextInt(500);
        Thread.sleep(millis);
        return "slept for " + millis + " millis";
    }

    @RequestMapping("/slowNonBlocking/{delay}")
    @ResponseBody
    public Mono<String> slowNonBlocking(@PathVariable("delay") Integer delay) {
        return Mono.just("Mono response, delay=" + delay).log().delayElement(Duration.ofMillis(delay));
    }
}
