package edu.jjxy.researchmanagementsystem.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectMember {
    private Long id;
    private Long projectId;
    private Long personId;
    private String duty;      // 职责：负责人 / 成员
    private LocalDate joinDate;
    private String remark;
    private String empNo;
    private String personName;

}
