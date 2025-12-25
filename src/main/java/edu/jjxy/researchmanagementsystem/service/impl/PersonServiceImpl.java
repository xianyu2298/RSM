package edu.jjxy.researchmanagementsystem.service.impl;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Person;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.mapper.PersonMapper;
import edu.jjxy.researchmanagementsystem.mapper.UserMapper;
import edu.jjxy.researchmanagementsystem.service.PersonService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonMapper personMapper;
    private final UserMapper userMapper;

    public PersonServiceImpl(PersonMapper personMapper, UserMapper userMapper) {
        this.personMapper = personMapper;
        this.userMapper = userMapper;
    }

    @Override
    public PageResult<Person> page(int page, int size, String name, String empNo) {
        int offset = (page - 1) * size;
        long total = personMapper.count(name, empNo);
        List<Person> records = personMapper.page(offset, size, name, empNo);
        return new PageResult<>(total, records);
    }


    @Override
    public Long add(Person p) {
        User u = new User();
        u.setUsername(p.getEmpNo());
        u.setRealName(p.getName());
        u.setEmpNo(p.getEmpNo());
        u.setGender(p.getGender());
        u.setTitle(p.getTitle());
        u.setDepartment(p.getDepartment());
        u.setPhone(p.getPhone());
        u.setEmail(p.getEmail());
        u.setHireDate(p.getHireDate());
        u.setRemark(p.getRemark());
        u.setRole("USER");
        u.setPasswordHash(DigestUtils.md5DigestAsHex("123456".getBytes()));
        u.setStatus(1);
        userMapper.insert(u);
        return u.getId();
    }

    @Override
    public void update(Person p) {
        User u = userMapper.selectById(p.getId());
        if (u == null) {
            return;
        }
        u.setRealName(p.getName());
        u.setEmpNo(p.getEmpNo());
        u.setGender(p.getGender());
        u.setTitle(p.getTitle());
        u.setDepartment(p.getDepartment());
        u.setPhone(p.getPhone());
        u.setEmail(p.getEmail());
        u.setHireDate(p.getHireDate());
        u.setRemark(p.getRemark());
        userMapper.update(u);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public Person get(Long id) {
        return personMapper.selectById(id);
    }
}
