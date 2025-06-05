package com.xvclemente.dnd.ms4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class Ms4HallOfFameApplication {

    public static void main(String[] args) {
        SpringApplication.run(Ms4HallOfFameApplication.class, args);
    }

}