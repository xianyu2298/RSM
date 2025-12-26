package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Project;
import edu.jjxy.researchmanagementsystem.entity.User;

import java.util.List;

public interface ProjectService {
    PageResult<Project> page(int page, int size, String name, String natureCode, String scopeCode);
    Long add(Project p);
    void update(Project p, User currentUser);
    void delete(Long id, User currentUser);
    Project get(Long id);
    List<Project> listByPersonId(Long personId);

}
