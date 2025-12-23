package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Book;

public interface BookService {
    PageResult<Book> page(int page, int size, String name, Long personId);
    Long add(Book b);
    void update(Book b);
    void delete(Long id);
    Book get(Long id);
}
