package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.entity.ProjectPaper;

import java.util.List;

public interface ProjectPaperService {
    void bind(Long projectId, Long paperId);
    void unbind(Long id);
    List<ProjectPaper> listByProjectId(Long projectId);
}

