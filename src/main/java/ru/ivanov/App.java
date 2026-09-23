package ru.ivanov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = "ru.ivanov")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
