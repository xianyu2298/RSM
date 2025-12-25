package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/page")
    public Result<PageResult<User>> page(@RequestParam(defaultValue="1") int page,
                                         @RequestParam(defaultValue="10") int size,
                                         @RequestParam(required=false) String username,
                                         @RequestParam(required=false) String realName,
                                         @RequestParam(required=false) String role,
                                         @RequestParam(required=false) Integer status) {
        return Result.ok(service.page(page, size, username, realName, role, status));
    }

    @PostMapping
    public Result<Long> add(@RequestBody User u) {
        return Result.ok(service.add(u));
    }

    @PutMapping
    public Result<Void> update(@RequestBody User u) {
        service.update(u);
        return Result.ok(null);
    }

    @PutMapping("/status")
    public Result<Void> status(@RequestParam Long id, @RequestParam Integer status) {
        service.enable(id, status);
        return Result.ok(null);
    }

    @PutMapping("/password")
    public Result<Void> changePwd(@RequestParam(required = false) Long userId,
                                  @RequestParam String oldPwd,
                                  @RequestParam String newPwd,
                                  HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        if (current == null) {
            throw new RuntimeException("未登录");
        }
        Long targetId = userId;
        if (!"ADMIN".equalsIgnoreCase(current.getRole())) {
            targetId = current.getId();
        } else {
            if (targetId == null) {
                targetId = current.getId();
            }
        }
        service.changePassword(targetId, oldPwd, newPwd);
        return Result.ok(null);
    }

    @PutMapping("/reset-password")
    public Result<Void> resetPassword(@RequestParam Long userId,
                                      @RequestParam String newPwd,
                                      HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        if (current == null || !"ADMIN".equalsIgnoreCase(current.getRole())) {
            throw new RuntimeException("无权限");
        }
        service.resetPassword(userId, newPwd);
        return Result.ok(null);
    }
}
