package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.entity.ProjectBook;
import edu.jjxy.researchmanagementsystem.entity.User;
import java.util.List;

public interface ProjectBookService {

    void bind(Long projectId, Long bookId, User currentUser);

    void unbind(Long id, User currentUser);

    List<ProjectBook> listByProjectId(Long projectId);
}
