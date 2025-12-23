package edu.jjxy.researchmanagementsystem.mapper;

import edu.jjxy.researchmanagementsystem.entity.ProjectMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMemberMapper {

    int insert(ProjectMember pm);

    int deleteById(@Param("id") Long id);

    List<ProjectMember> listByProjectId(@Param("projectId") Long projectId);
}
