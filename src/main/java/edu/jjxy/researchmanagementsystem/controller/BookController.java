package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.Book;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.service.BookService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/book")
public class BookController {
    private final BookService service;
    public BookController(BookService service){ this.service = service; }

    @GetMapping("/page")
    public Result<PageResult<Book>> page(@RequestParam(defaultValue="1") int page,
                                         @RequestParam(defaultValue="10") int size,
                                         @RequestParam(required=false) String name,
                                         @RequestParam(required=false) Long personId,
                                         HttpServletRequest request){
        return Result.ok(service.page(page, size, name, personId));
    }

    @PostMapping
    public Result<Long> add(@RequestBody Book b, HttpServletRequest request){
        User current = (User) request.getAttribute("currentUser");
        if (current != null && "USER".equalsIgnoreCase(current.getRole())) {
            b.setPersonId(current.getId());
        }
        return Result.ok(service.add(b));
    }
    @PutMapping
    public Result<Void> update(@RequestBody Book b, HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        service.update(b, current);
        return Result.ok(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        service.delete(id, current);
        return Result.ok(null);
    }
    @GetMapping("/{id}") public Result<Book> get(@PathVariable Long id){ return Result.ok(service.get(id)); }
}
