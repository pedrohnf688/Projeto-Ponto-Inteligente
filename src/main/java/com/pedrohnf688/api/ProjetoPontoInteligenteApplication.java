package com.pedrohnf688.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan("com.pedrohnf688.api")
public class ProjetoPontoInteligenteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoPontoInteligenteApplication.class, args);
	}

}
