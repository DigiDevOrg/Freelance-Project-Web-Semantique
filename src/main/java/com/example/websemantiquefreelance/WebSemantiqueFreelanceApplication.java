package com.example.websemantiquefreelance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@SpringBootApplication
public class WebSemantiqueFreelanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSemantiqueFreelanceApplication.class, args);
    }

}
