package edu.jjxy.researchmanagementsystem.service.impl;

import edu.jjxy.researchmanagementsystem.entity.ProjectMember;
import edu.jjxy.researchmanagementsystem.mapper.ProjectMemberMapper;
import edu.jjxy.researchmanagementsystem.service.ProjectMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberMapper mapper;

    public ProjectMemberServiceImpl(ProjectMemberMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Long add(ProjectMember pm) {
        mapper.insert(pm);
        return pm.getId();
    }

    @Override
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public List<ProjectMember> listByProject(Long projectId) {
        return mapper.listByProjectId(projectId);
    }
}
