package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.entity.DictItem;
import java.util.List;

public interface DictService {
    List<DictItem> listItemsByType(String typeCode);
}
