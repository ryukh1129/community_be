package com.meta.community_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Community_beApplication {

    public static void main(String[] args) {
        SpringApplication.run(Community_beApplication.class, args);
    }

}
