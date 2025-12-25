package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.entity.ProjectMember;
import edu.jjxy.researchmanagementsystem.entity.User;

import java.util.List;

public interface ProjectMemberService {
    Long add(ProjectMember pm, User currentUser);
    void delete(Long id, User currentUser);
    List<ProjectMember> listByProject(Long projectId);
}
