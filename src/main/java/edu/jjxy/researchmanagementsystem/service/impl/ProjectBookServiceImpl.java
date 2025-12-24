package edu.jjxy.researchmanagementsystem.service.impl;

import edu.jjxy.researchmanagementsystem.entity.ProjectBook;
import edu.jjxy.researchmanagementsystem.mapper.ProjectBookMapper;
import edu.jjxy.researchmanagementsystem.service.ProjectBookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectBookServiceImpl implements ProjectBookService {

    private final ProjectBookMapper mapper;

    public ProjectBookServiceImpl(ProjectBookMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void bind(Long projectId, Long bookId) {
        mapper.insert(projectId, bookId);
    }

    @Override
    public void unbind(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public List<ProjectBook> listByProjectId(Long projectId) {
        return mapper.listByProjectId(projectId);
    }
}
