package edu.jjxy.researchmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResp {
    private Long id;
    private String username;
    private String realName;
    private String role;
    private String token;
}
