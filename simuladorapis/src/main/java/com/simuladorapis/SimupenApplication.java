package com.simuladorapis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.simuladorapis")
@ComponentScan(basePackages = "com.simuladorapis")
public class SimupenApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimupenApplication.class, args);
    }
}