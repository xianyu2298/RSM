package edu.jjxy.researchmanagementsystem.mapper;

import edu.jjxy.researchmanagementsystem.entity.Person;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PersonMapper {
    List<Person> page(@Param("name") String name);
    int insert(Person p);
    int update(Person p);
    int deleteById(@Param("id") Long id);
    Person selectById(@Param("id") Long id);
}
