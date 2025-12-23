package edu.jjxy.researchmanagementsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.jjxy.researchmanagementsystem.mapper")
public class ResearchManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResearchManagementSystemApplication.class, args);
    }
}
