package com.gs.innerapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"com.gs.commons.mapper"})
@SpringBootApplication(scanBasePackages = "com.gs")
public class InnerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnerApiApplication.class, args);
    }

}
