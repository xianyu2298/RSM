package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.ProjectMember;
import edu.jjxy.researchmanagementsystem.service.ProjectMemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-member")
public class ProjectMemberController {

    private final ProjectMemberService service;

    public ProjectMemberController(ProjectMemberService service) {
        this.service = service;
    }

    // 添加项目成员
    @PostMapping
    public Result<Long> add(@RequestBody ProjectMember pm) {
        return Result.ok(service.add(pm));
    }

    // 查询某项目的所有成员
    @GetMapping("/list")
    public Result<List<ProjectMember>> list(@RequestParam Long projectId) {
        return Result.ok(service.listByProject(projectId));
    }

    // 移除成员
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok(null);
    }
}
