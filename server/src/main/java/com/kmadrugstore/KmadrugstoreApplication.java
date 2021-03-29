package com.kmadrugstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KmadrugstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmadrugstoreApplication.class, args);
    }
}
