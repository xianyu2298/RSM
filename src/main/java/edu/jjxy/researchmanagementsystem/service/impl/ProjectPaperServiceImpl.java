package edu.jjxy.researchmanagementsystem.service.impl;

import edu.jjxy.researchmanagementsystem.entity.Paper;
import edu.jjxy.researchmanagementsystem.entity.ProjectPaper;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.mapper.PaperMapper;
import edu.jjxy.researchmanagementsystem.mapper.ProjectMemberMapper;
import edu.jjxy.researchmanagementsystem.mapper.ProjectPaperMapper;
import edu.jjxy.researchmanagementsystem.service.ProjectPaperService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectPaperServiceImpl implements ProjectPaperService {

    private final ProjectPaperMapper mapper;
    private final ProjectMemberMapper projectMemberMapper;
    private final PaperMapper paperMapper;

    public ProjectPaperServiceImpl(ProjectPaperMapper mapper,
                                   ProjectMemberMapper projectMemberMapper,
                                   PaperMapper paperMapper) {
        this.mapper = mapper;
        this.projectMemberMapper = projectMemberMapper;
        this.paperMapper = paperMapper;
    }

    @Override
    public void bind(Long projectId, Long paperId, User currentUser) {
        if (currentUser != null && "USER".equalsIgnoreCase(currentUser.getRole())) {
            if (projectMemberMapper.countByProjectIdAndPersonId(projectId, currentUser.getId()) <= 0) {
                throw new RuntimeException("无权限：当前用户不是该项目成员");
            }
            Paper paper = paperMapper.selectById(paperId);
            if (paper == null || paper.getPersonId() == null || !paper.getPersonId().equals(currentUser.getId())) {
                throw new RuntimeException("无权限：只能绑定本人论文");
            }
        }
        mapper.insert(projectId, paperId);
    }

    @Override
    public void unbind(Long id, User currentUser) {
        if (currentUser != null && "USER".equalsIgnoreCase(currentUser.getRole())) {
            ProjectPaper pp = mapper.selectById(id);
            if (pp == null) {
                return;
            }
            if (projectMemberMapper.countByProjectIdAndPersonId(pp.getProjectId(), currentUser.getId()) <= 0) {
                throw new RuntimeException("无权限：当前用户不是该项目成员");
            }
            Paper paper = paperMapper.selectById(pp.getPaperId());
            if (paper == null || paper.getPersonId() == null || !paper.getPersonId().equals(currentUser.getId())) {
                throw new RuntimeException("无权限：只能解绑本人论文");
            }
        }
        mapper.deleteById(id);
    }

    @Override
    public List<ProjectPaper> listByProjectId(Long projectId) {
        return mapper.listByProjectId(projectId);
    }
}

