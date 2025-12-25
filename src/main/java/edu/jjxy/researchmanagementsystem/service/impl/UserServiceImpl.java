package edu.jjxy.researchmanagementsystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.mapper.UserMapper;
import edu.jjxy.researchmanagementsystem.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;

    public UserServiceImpl(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PageResult<User> page(int page, int size, String username) {
        PageHelper.startPage(page, size);
        PageInfo<User> info = new PageInfo<>(mapper.page(username));
        return new PageResult<>(info.getTotal(), info.getList());
    }

    @Override
    public Long add(User u) {
        // 初始密码：123456
        u.setPasswordHash(DigestUtils.md5DigestAsHex("123456".getBytes()));
        u.setStatus(1);
        mapper.insert(u);
        return u.getId();
    }

    @Override
    public void enable(Long id, Integer status) {
        mapper.updateStatus(id, status);
    }

    @Override
    public void changePassword(Long userId, String oldPwd, String newPwd) {
        User db = mapper.selectById(userId);
        String oldHash = DigestUtils.md5DigestAsHex(oldPwd.getBytes());
        if (!oldHash.equals(db.getPasswordHash())) {
            throw new RuntimeException("原密码错误");
        }
        mapper.updatePassword(userId,
                DigestUtils.md5DigestAsHex(newPwd.getBytes()));
    }

    @Override
    public void resetPassword(Long userId, String newPwd) {
        mapper.updatePassword(userId,
                DigestUtils.md5DigestAsHex(newPwd.getBytes()));
    }
}
