package edu.jjxy.researchmanagementsystem.service.impl;

import edu.jjxy.researchmanagementsystem.dto.LoginReq;
import edu.jjxy.researchmanagementsystem.dto.LoginResp;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.mapper.UserMapper;
import edu.jjxy.researchmanagementsystem.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    public AuthServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public LoginResp login(LoginReq req) {
        if (req == null || isBlank(req.getUsername()) || isBlank(req.getPassword())) {
            throw new RuntimeException("用户名或密码不能为空");
        }

        User db = userMapper.selectByUsername(req.getUsername());
        if (db == null) throw new RuntimeException("用户名不存在");

        if (db.getStatus() != null && db.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        String input = req.getPassword();
        String inputMd5 = md5(input);

        String stored = db.getPasswordHash();
        boolean ok;

        // ✅ 兼容：数据库里可能是明文 123456，也可能是 md5
        if (stored != null && stored.length() == 32 && stored.matches("^[a-fA-F0-9]{32}$")) {
            ok = stored.equalsIgnoreCase(inputMd5);
        } else {
            ok = stored != null && stored.equals(input);
            // ✅ 如果明文登录成功，自动升级成 MD5（以后就统一了）
            if (ok) {
                userMapper.updatePassword(db.getId(), inputMd5);
            }
        }

        if (!ok) throw new RuntimeException("密码错误");

        String token = UUID.randomUUID().toString().replace("-", "");
        return new LoginResp(db.getId(), db.getUsername(), db.getRealName(), db.getRole(), token);
    }

    private String md5(String s) {
        return DigestUtils.md5DigestAsHex(s.getBytes(StandardCharsets.UTF_8));
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
