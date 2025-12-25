package edu.jjxy.researchmanagementsystem.service.impl;

import edu.jjxy.researchmanagementsystem.entity.Project;
import edu.jjxy.researchmanagementsystem.entity.ProjectMember;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.mapper.ProjectMapper;
import edu.jjxy.researchmanagementsystem.mapper.ProjectMemberMapper;
import edu.jjxy.researchmanagementsystem.service.ProjectMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberMapper mapper;
    private final ProjectMapper projectMapper;

    public ProjectMemberServiceImpl(ProjectMemberMapper mapper, ProjectMapper projectMapper) {
        this.mapper = mapper;
        this.projectMapper = projectMapper;
    }

    @Override
    public Long add(ProjectMember pm, User currentUser) {
        if (currentUser != null && "USER".equalsIgnoreCase(currentUser.getRole())) {
            if (pm.getProjectId() == null) {
                throw new RuntimeException("项目ID不能为空");
            }
            Project project = projectMapper.selectById(pm.getProjectId());
            if (project == null) {
                throw new RuntimeException("项目不存在");
            }
            if (project.getLeaderPersonId() == null || !project.getLeaderPersonId().equals(currentUser.getId())) {
                throw new RuntimeException("无权限：只有负责人可以绑定成员");
            }
        }
        mapper.insert(pm);
        return pm.getId();
    }

    @Override
    public void delete(Long id, User currentUser) {
        if (currentUser != null && "USER".equalsIgnoreCase(currentUser.getRole())) {
            ProjectMember pm = mapper.selectById(id);
            if (pm == null) {
                return;
            }
            Project project = projectMapper.selectById(pm.getProjectId());
            if (project == null) {
                throw new RuntimeException("项目不存在");
            }
            if (project.getLeaderPersonId() == null || !project.getLeaderPersonId().equals(currentUser.getId())) {
                throw new RuntimeException("无权限：只有负责人可以移除成员");
            }
        }
        mapper.deleteById(id);
    }

    @Override
    public List<ProjectMember> listByProject(Long projectId) {
        return mapper.listByProjectId(projectId);
    }
}
