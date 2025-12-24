package edu.jjxy.researchmanagementsystem.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectPaper {
    private Long id;
    private Long projectId;
    private Long paperId;
    private LocalDateTime createdAt;

    // JOIN paper 表用于展示
    private String title;
    private String journal;
    private String indexCode;
    private LocalDate publishDate;
    private String doi;
}

