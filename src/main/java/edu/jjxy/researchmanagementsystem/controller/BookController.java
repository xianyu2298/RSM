package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.Book;
import edu.jjxy.researchmanagementsystem.service.BookService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
public class BookController {
    private final BookService service;
    public BookController(BookService service){ this.service = service; }

    @GetMapping("/page")
    public Result<PageResult<Book>> page(@RequestParam(defaultValue="1") int page,
                                         @RequestParam(defaultValue="10") int size,
                                         @RequestParam(required=false) String name,
                                         @RequestParam(required=false) Long personId){
        return Result.ok(service.page(page, size, name, personId));
    }

    @PostMapping public Result<Long> add(@RequestBody Book b){ return Result.ok(service.add(b)); }
    @PutMapping public Result<Void> update(@RequestBody Book b){ service.update(b); return Result.ok(null); }
    @DeleteMapping("/{id}") public Result<Void> delete(@PathVariable Long id){ service.delete(id); return Result.ok(null); }
    @GetMapping("/{id}") public Result<Book> get(@PathVariable Long id){ return Result.ok(service.get(id)); }
}
