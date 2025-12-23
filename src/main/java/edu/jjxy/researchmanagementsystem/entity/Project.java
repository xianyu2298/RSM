package edu.jjxy.researchmanagementsystem.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Project {
    private Long id;
    private String projectCode;
    private String name;

    // 字典编码（下拉框）
    private String natureCode;   // PROJECT_NATURE
    private String scopeCode;    // PROJECT_SCOPE
    private String statusCode;   // PROJECT_STATUS（可选）

    private LocalDate startDate;
    private LocalDate endDate;

    private Long leaderPersonId; // 负责人（可空）
    private BigDecimal budget;
    private String remark;
}
