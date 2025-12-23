package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Paper;

public interface PaperService {
    PageResult<Paper> page(int page, int size, String title, Long personId, String indexCode);
    Long add(Paper p);
    void update(Paper p);
    void delete(Long id);
    Paper get(Long id);
}
