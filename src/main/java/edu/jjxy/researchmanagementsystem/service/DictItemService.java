package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.DictItem;

import java.util.List;

public interface DictItemService {
    PageResult<DictItem> page(int page, int size, String typeCode, String itemCode, String itemName, Integer status);
    List<DictItem> items(String typeCode); // 业务页面 dictItems(typeCode)
    void add(DictItem t);
    void update(DictItem t);
    void delete(Long id);
}
