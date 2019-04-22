
package com.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@RestController
@RequestMapping("/mvc")
@RequiredArgsConstructor
public class MvcController {
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Data
    @RequiredArgsConstructor
    private static class Response {
        private final boolean success;
        private final long delayInMillis;
    }

    @GetMapping(value = "/blocking/{delay}")
    public Response blocking(@PathVariable("delay") long delay) throws InterruptedException {
        Thread.sleep(delay);
        return new Response(true, delay);
    }

    @GetMapping(value = "/nonBlocking/{delay}")
    public DeferredResult<Response> nonBlocking(@PathVariable("delay") int delay) {
        DeferredResult<Response> result = new DeferredResult<>();
        scheduler.schedule(
                () -> result.setResult(new Response(true, delay)),
                delay,
                MILLISECONDS);
        return result;
    }

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping(value = "/ui/users")
    public ResponseEntity<String> getUsers() {
        return restTemplate.getForEntity("http://localhost:8081/api/users", String.class);
    }
}