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
                                            @RequestParam(required = false) String statusCode,
                                            @RequestParam(required = false) Long personId,
                                            HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        Long leaderPersonId = personId;
        
        if (current != null && "USER".equalsIgnoreCase(current.getRole())) {
            // 普通用户如果传了 personId，只能查自己的
            if (personId != null && !personId.equals(current.getId())) {
                throw new RuntimeException("无权限查看他人项目列表");
            }
            // 如果是查看“审核中的项目”，前端会传 statusCode=AUDIT_PENDING
        }
        
        return Result.ok(projectService.page(page, size, name, natureCode, scopeCode, statusCode, leaderPersonId));
    }

    @PostMapping
    public Result<Long> add(@RequestBody Project p, HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        if (current != null && "USER".equalsIgnoreCase(current.getRole())) {
            p.setStatusCode("AUDIT_PENDING"); // 普通用户新增项目默认为待审核
            p.setLeaderPersonId(current.getId()); // 默认负责人为自己
        }
        return Result.ok(projectService.add(p));
    }

    @PostMapping("/audit")
    public Result<Void> audit(@RequestParam Long id, 
                             @RequestParam String statusCode, 
                             @RequestParam(required = false) String remark,
                             HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        if (current == null || !"ADMIN".equalsIgnoreCase(current.getRole())) {
            throw new RuntimeException("无权限：只有管理员可以审核项目");
        }
        projectService.audit(id, statusCode, remark);
        return Result.ok(null);
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
