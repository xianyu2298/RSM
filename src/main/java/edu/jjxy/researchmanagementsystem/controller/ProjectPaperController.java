package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.ProjectPaper;
import edu.jjxy.researchmanagementsystem.service.ProjectPaperService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project-paper")
public class ProjectPaperController {

    private final ProjectPaperService service;

    public ProjectPaperController(ProjectPaperService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public Result<List<ProjectPaper>> list(@RequestParam Long projectId) {
        return Result.ok(service.listByProjectId(projectId));
    }

    @PostMapping("/bind")
    public Result<Void> bind(@RequestBody Map<String, Long> body) {
        service.bind(body.get("projectId"), body.get("paperId"));
        return Result.ok(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> unbind(@PathVariable Long id) {
        service.unbind(id);
        return Result.ok(null);
    }
}

