package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
        io.github.cdimascio.dotenv.Dotenv.configure().load();
        SpringApplication.run(DemoApplication.class, args);
	}

}
