package com.tfmapp.boot.configTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.tfmapp.model.service.ServiceAuthLdap;

@Configuration
@ComponentScan(basePackages = {"com.tfmapp"})
@SpringBootApplication
public class AppConfig {

}
