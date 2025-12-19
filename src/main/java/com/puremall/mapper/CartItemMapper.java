package com.puremall.mapper;

/**
 * 购物车商品项Mapper接口
 * 用于购物车商品项数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.CartItem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
    // 根据ID查询购物车项
    @Select("SELECT * FROM cart_items WHERE id = #{id}")
    CartItem findById(Long id);
    
    // 根据用户ID查询购物车项
    @Select("SELECT * FROM cart_items WHERE userId = #{userId} ORDER BY createTime DESC")
    List<CartItem> findByUserId(Long userId);
    
    // 根据用户ID和商品ID查询购物车项
    @Select("SELECT * FROM cart_items WHERE userId = #{userId} AND productId = #{productId}")
    CartItem findByUserIdAndProductId(Long userId, Long productId);
    
    // 根据用户ID和商品ID、规格查询购物车项
    @Select("SELECT * FROM cart_items WHERE userId = #{userId} AND productId = #{productId} AND spec = #{spec}")
    CartItem findByUserIdAndProductIdAndSpec(Long userId, Long productId, String spec);
    
    // 插入购物车项
    @Insert("INSERT INTO cart_items(userId, productId, spec, name, price, quantity, imageUrl, createTime, updateTime) VALUES(#{userId}, #{productId}, #{spec}, #{name}, #{price}, #{quantity}, #{imageUrl}, NOW(), NOW())")
    int insert(CartItem cartItem);
    
    // 更新购物车项
    @Update("UPDATE cart_items SET name = #{name}, price = #{price}, quantity = #{quantity}, imageUrl = #{imageUrl}, updateTime = NOW() WHERE id = #{id}")
    int update(CartItem cartItem);
    
    // 更新购物车项数量
    @Update("UPDATE cart_items SET quantity = #{quantity}, updateTime = NOW() WHERE id = #{id}")
    int updateQuantity(Long id, Integer quantity);
    
    // 更新购物车项价格
    @Update("UPDATE cart_items SET price = #{price}, updateTime = NOW() WHERE id = #{id}")
    int updatePrice(Long id, Double price);
    
    // 根据ID删除购物车项
    @Delete("DELETE FROM cart_items WHERE id = #{id}")
    int deleteById(Long id);
    
    // 根据用户ID删除所有购物车项
    @Delete("DELETE FROM cart_items WHERE userId = #{userId}")
    int deleteByUserId(Long userId);
    
    // 根据用户ID和商品ID删除购物车项
    @Delete("DELETE FROM cart_items WHERE userId = #{userId} AND productId = #{productId}")
    int deleteByUserIdAndProductId(Long userId, Long productId);
    
    // 根据用户ID和商品ID、规格删除购物车项
    @Delete("DELETE FROM cart_items WHERE userId = #{userId} AND productId = #{productId} AND spec = #{spec}")
    int deleteByUserIdAndProductIdAndSpec(Long userId, Long productId, String spec);
    
    // 查询购物车项总数
    @Select("SELECT COUNT(*) FROM cart_items WHERE userId = #{userId}")
    int countByUserId(Long userId);
    
    // 查询购物车商品总数量
    @Select("SELECT SUM(quantity) FROM cart_items WHERE userId = #{userId}")
    Integer getTotalQuantityByUserId(Long userId);
    
    // 查询购物车总金额
    @Select("SELECT SUM(price * quantity) FROM cart_items WHERE userId = #{userId}")
    Double getTotalAmountByUserId(Long userId);
    
    // 查询用户选中的购物车项
    @Select("SELECT * FROM cart_items WHERE userId = #{userId} AND selected = 1 ORDER BY createTime DESC")
    List<CartItem> findSelectedByUserId(Long userId);
    
    // 根据用户ID删除选中的购物车项
    @Delete("DELETE FROM cart_items WHERE userId = #{userId} AND selected = 1")
    int deleteSelectedByUserId(Long userId);
}