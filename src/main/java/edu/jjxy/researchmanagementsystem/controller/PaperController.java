package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.Paper;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.service.PaperService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/paper")
public class PaperController {
    private final PaperService service;
    public PaperController(PaperService service){ this.service = service; }

    @GetMapping("/page")
    public Result<PageResult<Paper>> page(@RequestParam(defaultValue="1") int page,
                                          @RequestParam(defaultValue="10") int size,
                                          @RequestParam(required=false) String title,
                                          @RequestParam(required=false) Long personId,
                                          @RequestParam(required=false) String indexCode,
                                          HttpServletRequest request){
        return Result.ok(service.page(page, size, title, personId, indexCode));
    }

    @PostMapping
    public Result<Long> add(@RequestBody Paper p, HttpServletRequest request){
        User current = (User) request.getAttribute("currentUser");
        if (current != null && "USER".equalsIgnoreCase(current.getRole())) {
            p.setPersonId(current.getId());
        }
        return Result.ok(service.add(p));
    }
    @PutMapping
    public Result<Void> update(@RequestBody Paper p, HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        service.update(p, current);
        return Result.ok(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        service.delete(id, current);
        return Result.ok(null);
    }
    @GetMapping("/{id}") public Result<Paper> get(@PathVariable Long id){ return Result.ok(service.get(id)); }
}
