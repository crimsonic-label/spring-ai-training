package pl.atd.ollama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class OllamaApplication {

	public static void main(String[] args) {
		SpringApplication.run(OllamaApplication.class, args);
	}

}
