package com.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RestController
public class UserRespositoryController {
    @RequestMapping(value = "/api/users")
    public List<User> getUsers() {
        return getUsersFromRepository();
    }

    private List<User> getUsersFromRepository() {
        return LongStream
                .range(1, 30)
                .mapToObj(i -> new User(i, "John Smith " + i))
                .collect(Collectors.toList());
    }
}
