package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.dto.LoginReq;
import edu.jjxy.researchmanagementsystem.dto.LoginResp;
import edu.jjxy.researchmanagementsystem.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginResp> login(@RequestBody LoginReq req) {
        try {
            return Result.ok(authService.login(req));
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }
}
