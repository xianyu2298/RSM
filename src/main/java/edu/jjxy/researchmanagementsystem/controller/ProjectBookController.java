package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.ProjectBook;
import edu.jjxy.researchmanagementsystem.service.ProjectBookService;
import org.springframework.web.bind.annotation.*;

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
    public Result<Void> bind(@RequestBody Map<String, Long> body) {
        service.bind(body.get("projectId"), body.get("bookId"));
        return Result.ok(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> unbind(@PathVariable Long id) {
        service.unbind(id);
        return Result.ok(null);
    }
}
