package edu.jjxy.researchmanagementsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProjectBook {
    private Long id;
    private Long projectId;
    private Long bookId;
    private LocalDateTime createdAt;

    // JOIN book 表展示
    private String bookName;
    private String publisher;
    private String publishDate;
    private String isbn;
    private String name;
}

