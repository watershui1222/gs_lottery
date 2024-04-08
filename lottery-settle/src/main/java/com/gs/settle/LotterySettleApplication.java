package com.gs.settle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.gs")
@EnableScheduling
@MapperScan({"com.gs.commons.mapper"})
public class LotterySettleApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotterySettleApplication.class, args);
    }

}
