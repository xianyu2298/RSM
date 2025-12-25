package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.entity.ProjectPaper;
import edu.jjxy.researchmanagementsystem.entity.User;

import java.util.List;

public interface ProjectPaperService {
    void bind(Long projectId, Long paperId, User currentUser);
    void unbind(Long id, User currentUser);
    List<ProjectPaper> listByProjectId(Long projectId);
}

