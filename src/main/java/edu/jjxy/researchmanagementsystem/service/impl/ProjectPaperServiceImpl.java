package edu.jjxy.researchmanagementsystem.service.impl;

import edu.jjxy.researchmanagementsystem.entity.ProjectPaper;
import edu.jjxy.researchmanagementsystem.mapper.ProjectPaperMapper;
import edu.jjxy.researchmanagementsystem.service.ProjectPaperService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectPaperServiceImpl implements ProjectPaperService {

    private final ProjectPaperMapper mapper;

    public ProjectPaperServiceImpl(ProjectPaperMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void bind(Long projectId, Long paperId) {
        mapper.insert(projectId, paperId);
    }

    @Override
    public void unbind(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public List<ProjectPaper> listByProjectId(Long projectId) {
        return mapper.listByProjectId(projectId);
    }
}

