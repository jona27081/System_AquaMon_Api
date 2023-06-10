package org.aquamon.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class AquaMonApplication {

	public static void main(String[] args) {
		SpringApplication.run(AquaMonApplication.class, args);
	}

}
