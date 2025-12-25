package edu.jjxy.researchmanagementsystem.service.impl;

import edu.jjxy.researchmanagementsystem.entity.Book;
import edu.jjxy.researchmanagementsystem.entity.ProjectBook;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.mapper.BookMapper;
import edu.jjxy.researchmanagementsystem.mapper.ProjectBookMapper;
import edu.jjxy.researchmanagementsystem.mapper.ProjectMemberMapper;
import edu.jjxy.researchmanagementsystem.service.ProjectBookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectBookServiceImpl implements ProjectBookService {

    private final ProjectBookMapper mapper;
    private final ProjectMemberMapper projectMemberMapper;
    private final BookMapper bookMapper;

    public ProjectBookServiceImpl(ProjectBookMapper mapper,
                                  ProjectMemberMapper projectMemberMapper,
                                  BookMapper bookMapper) {
        this.mapper = mapper;
        this.projectMemberMapper = projectMemberMapper;
        this.bookMapper = bookMapper;
    }

    @Override
    public void bind(Long projectId, Long bookId, User currentUser) {
        if (currentUser != null && "USER".equalsIgnoreCase(currentUser.getRole())) {
            if (projectMemberMapper.countByProjectIdAndPersonId(projectId, currentUser.getId()) <= 0) {
                throw new RuntimeException("无权限：当前用户不是该项目成员");
            }
            Book book = bookMapper.selectById(bookId);
            if (book == null || book.getPersonId() == null || !book.getPersonId().equals(currentUser.getId())) {
                throw new RuntimeException("无权限：只能绑定本人著作");
            }
        }
        mapper.insert(projectId, bookId);
    }

    @Override
    public void unbind(Long id, User currentUser) {
        if (currentUser != null && "USER".equalsIgnoreCase(currentUser.getRole())) {
            ProjectBook pb = mapper.selectById(id);
            if (pb == null) {
                return;
            }
            if (projectMemberMapper.countByProjectIdAndPersonId(pb.getProjectId(), currentUser.getId()) <= 0) {
                throw new RuntimeException("无权限：当前用户不是该项目成员");
            }
            Book book = bookMapper.selectById(pb.getBookId());
            if (book == null || book.getPersonId() == null || !book.getPersonId().equals(currentUser.getId())) {
                throw new RuntimeException("无权限：只能解绑本人著作");
            }
        }
        mapper.deleteById(id);
    }

    @Override
    public List<ProjectBook> listByProjectId(Long projectId) {
        return mapper.listByProjectId(projectId);
    }
}
