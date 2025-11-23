package com.puremall.mapper;

/**
 * 用户Mapper接口
 * 用于用户数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 根据用户名查询用户
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);
    
    // 根据邮箱查询用户
    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);
    
    // 根据手机号查询用户
    @Select("SELECT * FROM users WHERE phone = #{phone}")
    User findByPhone(String phone);
    
    // 根据ID查询用户
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);
    
    // 插入用户
    @Insert("INSERT INTO users(username, password, email, phone, status, created_at, updated_at) VALUES(#{username}, #{password}, #{email}, #{phone}, #{status}, NOW(), NOW())")
    int insert(User user);
    
    // 更新用户信息
    @Update("UPDATE users SET password = #{password}, email = #{email}, phone = #{phone}, status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int update(User user);
    
    // 更新用户状态
    @Update("UPDATE users SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(Long id, Integer status);
    
    // 根据ID删除用户
    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(Long id);
    
    // 检查用户名是否存在
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int countByUsername(String username);
    
    // 检查邮箱是否存在
    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int countByEmail(String email);
    
    // 检查手机号是否存在
    @Select("SELECT COUNT(*) FROM users WHERE phone = #{phone}")
    int countByPhone(String phone);
}