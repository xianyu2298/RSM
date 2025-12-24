package edu.jjxy.researchmanagementsystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.DictType;
import edu.jjxy.researchmanagementsystem.mapper.DictItemMapper;
import edu.jjxy.researchmanagementsystem.mapper.DictTypeMapper;
import edu.jjxy.researchmanagementsystem.service.DictTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictTypeServiceImpl implements DictTypeService {

    private final DictTypeMapper typeMapper;
    private final DictItemMapper itemMapper;

    public DictTypeServiceImpl(DictTypeMapper typeMapper, DictItemMapper itemMapper) {
        this.typeMapper = typeMapper;
        this.itemMapper = itemMapper;
    }

    @Override
    public PageResult<DictType> page(int page, int size, String typeCode, String typeName, Integer status) {
        PageHelper.startPage(page, size);
        List<DictType> list = typeMapper.page(typeCode, typeName, status);
        PageInfo<DictType> info = new PageInfo<>(list);
        return new PageResult<>(info.getList(), info.getTotal());
    }

    @Override
    public List<DictType> listAll() {
        return typeMapper.listAll();
    }

    @Override
    public void add(DictType t) {
        if (t.getStatus() == null) t.setStatus(1);
        if (t.getSortNo() == null) t.setSortNo(0);
        typeMapper.insert(t);
    }

    @Override
    public void update(DictType t) {
        if (t.getStatus() == null) t.setStatus(1);
        if (t.getSortNo() == null) t.setSortNo(0);
        typeMapper.update(t);
    }

    @Override
    public void delete(Long id) {
        DictType db = typeMapper.getById(id);
        if (db != null) {
            itemMapper.deleteByTypeCode(db.getTypeCode());
        }
        typeMapper.deleteById(id);
    }
}
