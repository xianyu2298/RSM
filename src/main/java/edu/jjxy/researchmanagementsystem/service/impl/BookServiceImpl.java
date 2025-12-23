package edu.jjxy.researchmanagementsystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.Book;
import edu.jjxy.researchmanagementsystem.mapper.BookMapper;
import edu.jjxy.researchmanagementsystem.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookMapper mapper;
    public BookServiceImpl(BookMapper mapper){ this.mapper = mapper; }

    @Override
    public PageResult<Book> page(int page, int size, String name, Long personId) {
        PageHelper.startPage(page, size);
        PageInfo<Book> info = new PageInfo<>(mapper.page(name, personId));
        return new PageResult<>(info.getTotal(), info.getList());
    }
    @Override public Long add(Book b){ mapper.insert(b); return b.getId(); }
    @Override public void update(Book b){ mapper.update(b); }
    @Override public void delete(Long id){ mapper.deleteById(id); }
    @Override public Book get(Long id){ return mapper.selectById(id); }
}
