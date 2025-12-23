package edu.jjxy.researchmanagementsystem.mapper;

import edu.jjxy.researchmanagementsystem.entity.Paper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface PaperMapper {
    List<Paper> page(@Param("title") String title,
                     @Param("personId") Long personId,
                     @Param("indexCode") String indexCode);

    int insert(Paper p);
    int update(Paper p);
    int deleteById(@Param("id") Long id);
    Paper selectById(@Param("id") Long id);
}
