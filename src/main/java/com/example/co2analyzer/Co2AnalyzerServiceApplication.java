package com.example.co2analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.co2analyzer")
public class Co2AnalyzerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Co2AnalyzerServiceApplication.class, args);
    }
}