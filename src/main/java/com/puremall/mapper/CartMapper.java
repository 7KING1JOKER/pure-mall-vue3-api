package com.puremall.mapper;

/**
 * 购物车Mapper接口
 * 用于购物车数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    // 根据用户ID查询购物车
    @Select("SELECT * FROM carts WHERE user_id = #{userId}")
    Cart findByUserId(Long userId);
    
    // 根据ID查询购物车
    @Select("SELECT * FROM carts WHERE id = #{id}")
    Cart findById(Long id);
    
    // 插入购物车
    @Insert("INSERT INTO carts(user_id, created_at, updated_at) VALUES(#{userId}, NOW(), NOW())")
    int insert(Cart cart);
    
    // 更新购物车
    @Update("UPDATE carts SET updated_at = NOW() WHERE id = #{id}")
    int update(Cart cart);
    
    // 更新购物车总金额
    @Update("UPDATE carts SET total_amount = #{totalAmount}, updated_at = NOW() WHERE id = #{id}")
    int updateTotalAmount(Long id, Double totalAmount);
    
    // 根据ID删除购物车
    @Delete("DELETE FROM carts WHERE id = #{id}")
    int deleteById(Long id);
    
    // 根据用户ID删除购物车
    @Delete("DELETE FROM carts WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);
    
    // 检查用户是否已有购物车
    @Select("SELECT COUNT(*) FROM carts WHERE user_id = #{userId}")
    int existsByUserId(Long userId);
}