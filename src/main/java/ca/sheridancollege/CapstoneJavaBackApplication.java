package ca.sheridancollege;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CapstoneJavaBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneJavaBackApplication.class, args);
	}

}
