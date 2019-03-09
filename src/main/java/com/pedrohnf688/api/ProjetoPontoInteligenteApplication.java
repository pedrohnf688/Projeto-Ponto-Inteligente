package com.pedrohnf688.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.pedrohnf688.api")
public class ProjetoPontoInteligenteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoPontoInteligenteApplication.class, args);
	}

}
