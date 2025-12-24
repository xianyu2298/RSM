package edu.jjxy.researchmanagementsystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Award;
import edu.jjxy.researchmanagementsystem.mapper.AwardMapper;
import edu.jjxy.researchmanagementsystem.service.AwardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AwardServiceImpl implements AwardService {
    private final AwardMapper mapper;
    public AwardServiceImpl(AwardMapper mapper){ this.mapper = mapper; }

    @Override
    public PageResult<Award> page(int page, int size, String awardName, Long projectId) {
        PageHelper.startPage(page, size);
        PageInfo<Award> info = new PageInfo<>(mapper.page(awardName, projectId));
        return new PageResult<>(info.getTotal(), info.getList());
    }
    @Override public Long add(Award a){ mapper.insert(a); return a.getId(); }
    @Override public void update(Award a){ mapper.update(a); }
    @Override public void delete(Long id){ mapper.deleteById(id); }
    @Override public Award get(Long id){ return mapper.selectById(id); }
    @Override
    public List<Award> listByPersonId(Long personId) {
        return mapper.listByPersonId(personId);
    }
}
