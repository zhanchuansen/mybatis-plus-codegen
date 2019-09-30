package com.suncompass.basic.codegen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



@ComponentScan(basePackages = {"com.suncompass.basic.codegen"})
@MapperScan("com.suncompass.basic.codegen.mapper")
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@SpringBootApplication
public class PigCodeGenApplication {
    public static void main(String[] args) {
        SpringApplication.run(PigCodeGenApplication.class, args);
    }
}