package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.entity.ProjectBook;
import java.util.List;

public interface ProjectBookService {

    void bind(Long projectId, Long bookId);

    void unbind(Long id);

    List<ProjectBook> listByProjectId(Long projectId);
}
