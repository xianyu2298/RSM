package edu.jjxy.researchmanagementsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String realName;
    private String role;      // ADMIN / USER
    private Integer status;   // 1启用 0禁用
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
