package co.edu.unicauca.api_rest;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiRestApplication {

	public static void main(String[] args) {
        SpringApplication.run(ApiRestApplication.class, args);
    }

    // Configura ModelMapper como un Bean de Spring
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
