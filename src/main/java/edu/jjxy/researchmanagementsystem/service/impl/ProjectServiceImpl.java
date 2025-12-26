package edu.jjxy.researchmanagementsystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Project;
import edu.jjxy.researchmanagementsystem.entity.ProjectMember;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.mapper.ProjectMapper;
import edu.jjxy.researchmanagementsystem.mapper.ProjectMemberMapper;
import edu.jjxy.researchmanagementsystem.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectMemberMapper projectMemberMapper;

    public ProjectServiceImpl(ProjectMapper projectMapper, ProjectMemberMapper projectMemberMapper) {
        this.projectMapper = projectMapper;
        this.projectMemberMapper = projectMemberMapper;
    }

    @Override
    public PageResult<Project> page(int page, int size, String name, String natureCode, String scopeCode) {
        PageHelper.startPage(page, size);
        PageInfo<Project> info = new PageInfo<>(projectMapper.page(name, natureCode, scopeCode));
        return new PageResult<>(info.getTotal(), info.getList());
    }

    @Override
    @Transactional
    public Long add(Project p) {
        projectMapper.insert(p);
        if (p.getLeaderPersonId() != null) {
            ProjectMember pm = new ProjectMember();
            pm.setProjectId(p.getId());
            pm.setPersonId(p.getLeaderPersonId());
            pm.setDuty("负责人");
            pm.setJoinDate(p.getStartDate() != null ? p.getStartDate() : LocalDate.now());
            pm.setRemark("创建项目自动绑定");
            projectMemberMapper.insert(pm);
        }
        return p.getId();
    }

    @Override
    public void update(Project p, User currentUser) {
        if (currentUser != null && "USER".equalsIgnoreCase(currentUser.getRole())) {
            Project db = projectMapper.selectById(p.getId());
            if (db == null) {
                throw new RuntimeException("项目不存在");
            }
            if (db.getLeaderPersonId() == null || !db.getLeaderPersonId().equals(currentUser.getId())) {
                throw new RuntimeException("无权限：只有负责人可以修改项目");
            }
        }
        projectMapper.update(p);
    }

    @Override
    public void delete(Long id, User currentUser) {
        if (currentUser != null && "USER".equalsIgnoreCase(currentUser.getRole())) {
            Project db = projectMapper.selectById(id);
            if (db == null) {
                throw new RuntimeException("项目不存在");
            }
            if (db.getLeaderPersonId() == null || !db.getLeaderPersonId().equals(currentUser.getId())) {
                throw new RuntimeException("无权限：只有负责人可以删除项目");
            }
        }
        projectMapper.deleteById(id);
    }

    @Override
    public Project get(Long id) {
        return projectMapper.selectById(id);
    }

    @Override
    public List<Project> listByPersonId(Long personId) {
        return projectMapper.listByPersonId(personId);
    }

}
