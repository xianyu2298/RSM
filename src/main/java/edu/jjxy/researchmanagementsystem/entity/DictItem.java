package edu.jjxy.researchmanagementsystem.entity;

import lombok.Data;

@Data
public class DictItem {
    private Long id;
    private String typeCode;
    private String itemCode;
    private String itemName;
    private Integer status;
    private Integer sortNo;
}
