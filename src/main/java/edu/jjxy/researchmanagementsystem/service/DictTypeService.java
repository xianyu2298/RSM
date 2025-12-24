package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.DictType;

import java.util.List;

public interface DictTypeService {
    PageResult<DictType> page(int page, int size, String typeCode, String typeName, Integer status);
    List<DictType> listAll();
    void add(DictType t);
    void update(DictType t);
    void delete(Long id);
}
