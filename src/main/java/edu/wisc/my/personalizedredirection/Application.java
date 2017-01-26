package edu.wisc.my.personalizedredirection;

import java.util.Arrays;

import edu.wisc.my.personalizedredirection.controller.PersonalizedRedirectionController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        PersonalizedRedirectionController prc = new PersonalizedRedirectionController();

        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return null;

    }

}
