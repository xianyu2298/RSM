package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.entity.ProjectMember;
import java.util.List;

public interface ProjectMemberService {
    Long add(ProjectMember pm);
    void delete(Long id);
    List<ProjectMember> listByProject(Long projectId);
}
