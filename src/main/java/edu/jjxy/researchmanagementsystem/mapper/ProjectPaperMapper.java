package edu.jjxy.researchmanagementsystem.mapper;

import edu.jjxy.researchmanagementsystem.entity.ProjectPaper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ProjectPaperMapper {
    int insert(@Param("projectId") Long projectId, @Param("paperId") Long paperId);
    int deleteById(@Param("id") Long id);
    List<ProjectPaper> listByProjectId(@Param("projectId") Long projectId);
}

