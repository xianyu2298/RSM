package edu.jjxy.researchmanagementsystem.mapper;

import edu.jjxy.researchmanagementsystem.entity.ProjectBook;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ProjectBookMapper {
    int insert(@Param("projectId") Long projectId, @Param("bookId") Long bookId);
    int deleteById(@Param("id") Long id);
    List<ProjectBook> listByProjectId(@Param("projectId") Long projectId);
}

