package com.test;

import com.test.service.LocalResponseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(MvcApplication.class, args);
    }


    @Bean
    public LocalResponseService localResponseService(){
        return new LocalResponseService();
    }

}
