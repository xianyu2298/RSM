package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.Project;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/page")
    public Result<PageResult<Project>> page(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String name,
                                            @RequestParam(required = false) String natureCode,
                                            @RequestParam(required = false) String scopeCode,
                                            HttpServletRequest request) {
        return Result.ok(projectService.page(page, size, name, natureCode, scopeCode));
    }

    @PostMapping
    public Result<Long> add(@RequestBody Project p) {
        return Result.ok(projectService.add(p));
    }

    @PutMapping
    public Result<Void> update(@RequestBody Project p, HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        projectService.update(p, current);
        return Result.ok(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        projectService.delete(id, current);
        return Result.ok(null);
    }

    @GetMapping("/{id}")
    public Result<Project> get(@PathVariable Long id) {
        return Result.ok(projectService.get(id));
    }

    @GetMapping("/listByPersonId")
    public Result<List<Project>> listByPersonId(@RequestParam Long personId, HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        Long effectivePersonId = personId;
        if (current != null && "USER".equalsIgnoreCase(current.getRole())) {
            effectivePersonId = current.getId();
        }
        return Result.ok(projectService.listByPersonId(effectivePersonId));
    }


}
