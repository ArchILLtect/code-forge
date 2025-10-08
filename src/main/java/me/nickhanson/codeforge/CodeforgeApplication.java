package me.nickhanson.codeforge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Codeforge application.
 */
@SpringBootApplication
public class CodeforgeApplication {
    /**
     * The main method to run the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CodeforgeApplication.class, args);
    }
}