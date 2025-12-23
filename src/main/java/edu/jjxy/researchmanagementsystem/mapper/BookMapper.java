package edu.jjxy.researchmanagementsystem.mapper;

import edu.jjxy.researchmanagementsystem.entity.Book;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface BookMapper {
    List<Book> page(@Param("name") String name, @Param("personId") Long personId);
    int insert(Book b);
    int update(Book b);
    int deleteById(@Param("id") Long id);
    Book selectById(@Param("id") Long id);
}
