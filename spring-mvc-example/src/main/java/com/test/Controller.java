
package com.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/mvc")
@RequiredArgsConstructor
public class Controller {

    private final UsersRepository usersRepository;
    private final LocalResponseService localResponseService;

    @GetMapping(value = "/blocking/{delay}")
    public LocalResponseService.Response blocking(@PathVariable("delay") int delay) {
        return localResponseService.blocking(delay).block();
    }

    @GetMapping(value = "/nonBlocking/{delay}")
    public LocalResponseService.Response nonBlocking(@PathVariable("delay") int delay) {
        return localResponseService.nonBlocking(delay).block();
    }

    @GetMapping(value = "/legacyBlocking/{delay}")
    public LocalResponseService.Response legacyBlocking(@PathVariable("delay") int delay) {
        return Mono.fromCallable(() -> localResponseService.blockingLegacy(delay))
                .subscribeOn(Schedulers.elastic()).block();
    }

    @GetMapping(value = "/ui/users")
    public String getUsers() {
        return usersRepository.users().block();
    }
}