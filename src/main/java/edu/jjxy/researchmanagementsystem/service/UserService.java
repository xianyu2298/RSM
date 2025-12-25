package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.entity.User;

public interface UserService {
    PageResult<User> page(int page, int size, String username);
    Long add(User u);
    void update(User u);
    void enable(Long id, Integer status);
    void changePassword(Long userId, String oldPwd, String newPwd);
    void resetPassword(Long userId, String newPwd);
}
