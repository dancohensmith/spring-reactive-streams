package com.test;

import com.test.service.LocalResponseService;
import com.test.model.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@RestController
@RequestMapping("/controller")
public class WebfluxController {

    private final UsersRepository usersRepository;
    private final LocalResponseService localResponseService;

    @GetMapping(value = "/blocking/{delay}")
    @ResponseBody
    public Mono<LocalResponseService.Response> blocking(@PathVariable("delay") int delay) {
        return localResponseService.blocking(delay);
    }

    @GetMapping(value = "/nonBlocking/{delay}")
    @ResponseBody
    public Mono<LocalResponseService.Response> nonBlocking(@PathVariable("delay") int delay) {
        return localResponseService.nonBlocking(delay);
    }

    @GetMapping(value = "/legacyBlocking/{delay}")
    @ResponseBody
    public Mono<LocalResponseService.Response> legacyBlocking(@PathVariable("delay") int delay) {
       return Mono.fromCallable(() -> localResponseService.blockingLegacy(delay)).subscribeOn(Schedulers.elastic());

    }

    @GetMapping(value = "/ui/users")
    @ResponseBody
    public Mono<String> getUsers() {
        return usersRepository.users();
    }
}
