package com.gs.paycallback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"com.gs.commons.mapper"})
@SpringBootApplication(scanBasePackages = "com.gs")
public class PayCallbackiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayCallbackiApplication.class, args);
    }

}
