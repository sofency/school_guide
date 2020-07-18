package com.xpu.school_guide;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@MapperScan("com.xpu.school_guide.mapper")
@SpringBootApplication
@EnableTransactionManagement
@EnableWebMvc
public class SchoolGuideApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchoolGuideApplication.class, args);
    }

}
