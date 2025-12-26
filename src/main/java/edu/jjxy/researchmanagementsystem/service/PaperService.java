package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Paper;
import edu.jjxy.researchmanagementsystem.entity.User;

public interface PaperService {
    PageResult<Paper> page(int page, int size, String title, Long personId, String indexCode);
    Long add(Paper p);
    void update(Paper p, User currentUser);
    void delete(Long id, User currentUser);
    Paper get(Long id);
}
