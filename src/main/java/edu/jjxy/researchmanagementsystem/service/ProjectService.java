package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Project;

import java.util.List;

public interface ProjectService {
    PageResult<Project> page(int page, int size, String name, String natureCode, String scopeCode);
    Long add(Project p);
    void update(Project p);
    void delete(Long id);
    Project get(Long id);
    List<Project> listByPersonId(Long personId);

}
