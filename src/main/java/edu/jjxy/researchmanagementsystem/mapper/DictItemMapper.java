package edu.jjxy.researchmanagementsystem.mapper;

import edu.jjxy.researchmanagementsystem.entity.DictItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictItemMapper {
    List<DictItem> page(@Param("typeCode") String typeCode,
                        @Param("itemCode") String itemCode,
                        @Param("itemName") String itemName,
                        @Param("status") Integer status);

    // 业务页面使用：按 typeCode 获取“启用”的字典项
    List<DictItem> listEnabledByTypeCode(@Param("typeCode") String typeCode);

    Integer maxSortNoByTypeCode(@Param("typeCode") String typeCode);

    int insert(DictItem t);
    int update(DictItem t);
    int deleteById(@Param("id") Long id);

    int deleteByTypeCode(@Param("typeCode") String typeCode);
}
