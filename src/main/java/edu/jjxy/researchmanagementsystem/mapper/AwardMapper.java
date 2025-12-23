package edu.jjxy.researchmanagementsystem.mapper;

import edu.jjxy.researchmanagementsystem.entity.Award;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface AwardMapper {
    List<Award> page(@Param("awardName") String awardName, @Param("projectId") Long projectId);
    int insert(Award a);
    int update(Award a);
    int deleteById(@Param("id") Long id);
    Award selectById(@Param("id") Long id);
}
