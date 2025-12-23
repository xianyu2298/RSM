package edu.jjxy.researchmanagementsystem.service.impl;

import edu.jjxy.researchmanagementsystem.entity.DictItem;
import edu.jjxy.researchmanagementsystem.mapper.DictMapper;
import edu.jjxy.researchmanagementsystem.service.DictService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    private final DictMapper dictMapper;

    public DictServiceImpl(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public List<DictItem> listItemsByType(String typeCode) {
        return dictMapper.listItemsByType(typeCode);
    }
}
