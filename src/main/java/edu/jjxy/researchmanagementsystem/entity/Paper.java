package edu.jjxy.researchmanagementsystem.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Paper {
    private Long id;
    private Long personId;
    private String title;
    private String journal;
    private String indexCode;     // EI/SCI/CORE/NORMAL (字典)
    private LocalDate publishDate;
    private String doi;
    private String filePath;
    private String remark;
    private String empNo;
    private String personName;
}
