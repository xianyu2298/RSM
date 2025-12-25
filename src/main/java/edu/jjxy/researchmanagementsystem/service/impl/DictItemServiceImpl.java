package edu.jjxy.researchmanagementsystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.DictItem;
import edu.jjxy.researchmanagementsystem.mapper.DictItemMapper;
import edu.jjxy.researchmanagementsystem.service.DictItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictItemServiceImpl implements DictItemService {

    private final DictItemMapper mapper;

    public DictItemServiceImpl(DictItemMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PageResult<DictItem> page(int page, int size, String typeCode, String itemCode, String itemName, Integer status) {
        PageHelper.startPage(page, size);
        List<DictItem> list = mapper.page(typeCode, itemCode, itemName, status);
        PageInfo<DictItem> info = new PageInfo<>(list);
        return new PageResult<>(info.getList(), info.getTotal());
    }

    @Override
    public List<DictItem> items(String typeCode) {
        return mapper.listEnabledByTypeCode(typeCode);
    }

    @Override
    public void add(DictItem t) {
        if (t.getStatus() == null) t.setStatus(1);
        if (t.getSortNo() == null) {
            Integer max = mapper.maxSortNoByTypeCode(t.getTypeCode());
            int next = (max == null ? 0 : max) + 1;
            t.setSortNo(next);
        }
        mapper.insert(t);
    }

    @Override
    public void update(DictItem t) {
        if (t.getStatus() == null) t.setStatus(1);
        if (t.getSortNo() == null) t.setSortNo(0);
        mapper.update(t);
    }

    @Override
    public void delete(Long id) {
        mapper.deleteById(id);
    }
}
