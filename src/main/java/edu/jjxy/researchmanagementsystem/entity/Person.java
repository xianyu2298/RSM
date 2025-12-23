package edu.jjxy.researchmanagementsystem.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Person {
    private Long id;
    private String empNo;
    private String name;
    private String gender;
    private String title;
    private String department;
    private String phone;
    private String email;
    private LocalDate hireDate;
    private String remark;
}
