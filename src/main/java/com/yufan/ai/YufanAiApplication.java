package com.yufan.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.yufan.ai.mapper")
@SpringBootApplication
public class YufanAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YufanAiApplication.class, args);
    }

}
