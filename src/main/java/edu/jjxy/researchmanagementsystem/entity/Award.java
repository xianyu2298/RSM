package edu.jjxy.researchmanagementsystem.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Award {
    private Long id;
    private Long projectId;     // 可为空
    private String awardName;
    private String awardLevel;
    private String awardOrg;
    private LocalDate awardDate;
    private String remark;
    private String projectCode;
    private String projectName;
    private String awardPersons; // 获奖人（项目成员拼接）

}
