package edu.jjxy.researchmanagementsystem.mapper;

import edu.jjxy.researchmanagementsystem.entity.DictItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictMapper {
    List<DictItem> listItemsByType(@Param("typeCode") String typeCode);
}
