package com.test;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class MvcController {
    private RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/blocking/{delay}")
    @ResponseBody
    public String blocking(@PathVariable("delay") int delay) throws InterruptedException {
        Thread.sleep(delay);
        // reading from a file system or traditional database
        return createResponse(delay);
    }

    @RequestMapping(value = "/nonBlocking/{delay}")
    @ResponseBody
    public Mono<String> nonBlocking(@PathVariable("delay") int delay) {
        return Mono
                .just(createResponse(delay))
                //.log()
                .delayElement(Duration.ofMillis(delay));
    }

    private String createResponse(int delay) {
        return "{\"success\": true, \"delayInMillis\": " + delay + "}";
    }

    @RequestMapping(value = "/ui/users")
    @ResponseBody
    public Mono<String> getUsers() {
        return Mono.just(restTemplate.getForObject("http://localhost:8081/api/users", String.class));
    }
}
