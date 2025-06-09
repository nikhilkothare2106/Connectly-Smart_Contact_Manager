package com.scm.SCMProject;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScmProjectApplication {
    Dotenv dotenv = Dotenv.configure().directory("./").load();

    public static void main(String[] args) {
        SpringApplication.run(ScmProjectApplication.class, args);
    }

}
