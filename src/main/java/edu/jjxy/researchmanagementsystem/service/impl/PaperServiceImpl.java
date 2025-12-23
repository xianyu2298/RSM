package edu.jjxy.researchmanagementsystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Paper;
import edu.jjxy.researchmanagementsystem.mapper.PaperMapper;
import edu.jjxy.researchmanagementsystem.service.PaperService;
import org.springframework.stereotype.Service;

@Service
public class PaperServiceImpl implements PaperService {
    private final PaperMapper mapper;
    public PaperServiceImpl(PaperMapper mapper) { this.mapper = mapper; }

    @Override
    public PageResult<Paper> page(int page, int size, String title, Long personId, String indexCode) {
        PageHelper.startPage(page, size);
        PageInfo<Paper> info = new PageInfo<>(mapper.page(title, personId, indexCode));
        return new PageResult<>(info.getTotal(), info.getList());
    }
    @Override public Long add(Paper p){ mapper.insert(p); return p.getId(); }
    @Override public void update(Paper p){ mapper.update(p); }
    @Override public void delete(Long id){ mapper.deleteById(id); }
    @Override public Paper get(Long id){ return mapper.selectById(id); }
}
