package edu.jjxy.researchmanagementsystem.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Book {
    private Long id;
    private Long personId;
    private String name;
    private String publisher;
    private LocalDate publishDate;
    private String isbn;
    private String remark;
}
