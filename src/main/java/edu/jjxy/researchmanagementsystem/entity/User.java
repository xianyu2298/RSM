package edu.jjxy.researchmanagementsystem.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String realName;
    private String empNo;
    private String gender;
    private String title;
    private String department;
    private String phone;
    private String email;
    private LocalDate hireDate;
    private String remark;
    private String role;      // ADMIN / USER
    private Integer status;   // 1启用 0禁用
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
