package com.cyberark.sbtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
    	
//    	AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext();
//        ConfigurableEnvironment env = context.getEnvironment();
//        env.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("app.properties")));
//        
//        
//        String contactPerson = env.getProperty("contact-person");
    	
    	
    	
        SpringApplication.run(App.class, args);
    }
}