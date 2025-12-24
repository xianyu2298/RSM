package edu.jjxy.researchmanagementsystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Project;
import edu.jjxy.researchmanagementsystem.mapper.ProjectMapper;
import edu.jjxy.researchmanagementsystem.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Override
    public PageResult<Project> page(int page, int size, String name, String natureCode, String scopeCode) {
        PageHelper.startPage(page, size);
        PageInfo<Project> info = new PageInfo<>(projectMapper.page(name, natureCode, scopeCode));
        return new PageResult<>(info.getTotal(), info.getList());
    }

    @Override
    public Long add(Project p) {
        projectMapper.insert(p);
        return p.getId();
    }

    @Override
    public void update(Project p) {
        projectMapper.update(p);
    }

    @Override
    public void delete(Long id) {
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
