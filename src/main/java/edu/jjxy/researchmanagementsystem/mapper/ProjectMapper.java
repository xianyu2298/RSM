package edu.jjxy.researchmanagementsystem.mapper;

import edu.jjxy.researchmanagementsystem.entity.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMapper {
    List<Project> page(@Param("name") String name,
                       @Param("natureCode") String natureCode,
                       @Param("scopeCode") String scopeCode);

    int insert(Project p);
    int update(Project p);
    int deleteById(@Param("id") Long id);
    Project selectById(@Param("id") Long id);
    List<Project> listByPersonId(@Param("personId") Long personId);

}
