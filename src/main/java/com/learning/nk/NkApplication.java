package com.learning.nk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class})
@ComponentScan({"com.learning.nk*"})
@EntityScan("com.learning.nk*")
public class NkApplication {
    public static void main(String[] args) {
        SpringApplication.run(NkApplication.class , args);
    }

}
