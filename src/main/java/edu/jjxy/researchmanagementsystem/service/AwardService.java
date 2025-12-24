package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Award;

import java.util.List;

public interface AwardService {
    PageResult<Award> page(int page, int size, String awardName, Long projectId);
    Long add(Award a);
    void update(Award a);
    void delete(Long id);
    Award get(Long id);
    List<Award> listByPersonId(Long personId);
}
