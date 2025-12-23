package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.service.UserService;
import org.springframework.web.bind.annotation.*;

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
                                         @RequestParam(required=false) String username) {
        return Result.ok(service.page(page, size, username));
    }

    @PostMapping
    public Result<Long> add(@RequestBody User u) {
        return Result.ok(service.add(u));
    }

    @PutMapping("/status")
    public Result<Void> status(@RequestParam Long id, @RequestParam Integer status) {
        service.enable(id, status);
        return Result.ok(null);
    }

    @PutMapping("/password")
    public Result<Void> changePwd(@RequestParam Long userId,
                                  @RequestParam String oldPwd,
                                  @RequestParam String newPwd) {
        service.changePassword(userId, oldPwd, newPwd);
        return Result.ok(null);
    }
}
