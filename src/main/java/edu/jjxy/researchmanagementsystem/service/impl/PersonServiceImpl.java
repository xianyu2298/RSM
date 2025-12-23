package edu.jjxy.researchmanagementsystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Person;
import edu.jjxy.researchmanagementsystem.mapper.PersonMapper;
import edu.jjxy.researchmanagementsystem.service.PersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonMapper personMapper;

    public PersonServiceImpl(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    @Override
    public PageResult<Person> page(int page, int size, String name) {
        PageHelper.startPage(page, size);
        PageInfo<Person> info = new PageInfo<>(personMapper.page(name));
        return new PageResult<>(info.getTotal(), info.getList());
    }

    @Override
    public Long add(Person p) {
        personMapper.insert(p);
        return p.getId();
    }

    @Override
    public void update(Person p) {
        personMapper.update(p);
    }

    @Override
    public void delete(Long id) {
        personMapper.deleteById(id);
    }

    @Override
    public Person get(Long id) {
        return personMapper.selectById(id);
    }
}
