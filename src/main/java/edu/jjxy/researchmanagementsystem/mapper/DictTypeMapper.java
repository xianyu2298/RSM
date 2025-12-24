package edu.jjxy.researchmanagementsystem.mapper;

import edu.jjxy.researchmanagementsystem.entity.DictType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictTypeMapper {
    List<DictType> page(@Param("typeCode") String typeCode,
                        @Param("typeName") String typeName,
                        @Param("status") Integer status);

    List<DictType> listAll();

    DictType getById(@Param("id") Long id);

    int insert(DictType t);
    int update(DictType t);
    int deleteById(@Param("id") Long id);
}
