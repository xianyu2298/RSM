package edu.jjxy.researchmanagementsystem.mapper;

import edu.jjxy.researchmanagementsystem.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserMapper {
    List<User> page(@Param("username") String username);
    int insert(User u);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int updatePassword(@Param("id") Long id, @Param("passwordHash") String passwordHash);
    User selectById(@Param("id") Long id);
    User selectByUsername(@Param("username") String username);
}
