package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Book;
import edu.jjxy.researchmanagementsystem.entity.User;

public interface BookService {
    PageResult<Book> page(int page, int size, String name, Long personId);
    Long add(Book b);
    void update(Book b, User currentUser);
    void delete(Long id, User currentUser);
    Book get(Long id);
}
