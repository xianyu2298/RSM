package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.ProjectBook;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.service.ProjectBookService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project-book")
public class ProjectBookController {

    private final ProjectBookService service;

    public ProjectBookController(ProjectBookService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public Result<List<ProjectBook>> list(@RequestParam Long projectId) {
        return Result.ok(service.listByProjectId(projectId));
    }

    @PostMapping("/bind")
    public Result<Void> bind(@RequestBody Map<String, Long> body, HttpServletRequest request) {
        service.bind(body.get("projectId"), body.get("bookId"), (User) request.getAttribute("currentUser"));
        return Result.ok(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> unbind(@PathVariable Long id, HttpServletRequest request) {
        service.unbind(id, (User) request.getAttribute("currentUser"));
        return Result.ok(null);
    }
}
