package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Person;

public interface PersonService {
    PageResult<Person> page(int page, int size, String name, String empNo);
    Long add(Person p);
    void update(Person p);
    void delete(Long id);
    Person get(Long id);
}
