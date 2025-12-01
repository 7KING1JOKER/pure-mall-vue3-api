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
    // 根据购物车ID查询购物车项列表
    @Select("SELECT * FROM cart_items WHERE cartId = #{cartId} ORDER BY createTime DESC")
    List<CartItem> findByCartId(Long cartId);
    
    // 根据购物车ID、商品ID和规格ID查询购物车项
    @Select("SELECT * FROM cart_items WHERE cartId = #{cartId} AND productId = #{productId} AND specId = #{specId}")
    CartItem findByCartIdAndProductIdAndSpecId(Long cartId, Long productId, Long specId);
    
    // 根据ID查询购物车项
    @Select("SELECT * FROM cart_items WHERE id = #{id}")
    CartItem findById(Long id);
    
    // 根据用户ID查询购物车项（通过关联查询）
    @Select("SELECT ci.* FROM cart_items ci JOIN carts c ON ci.cartId = c.id WHERE c.userId = #{userId} ORDER BY ci.createTime DESC")
    List<CartItem> findByUserId(Long userId);
    
    // 插入购物车项
    @Insert("INSERT INTO cart_items(cartId, productId, specId, name, price, quantity, imageUrl, createTime, updateTime) VALUES(#{cartId}, #{productId}, #{specId}, #{name}, #{price}, #{quantity}, #{imageUrl}, NOW(), NOW())")
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
    
    // 根据购物车ID删除所有购物车项
    @Delete("DELETE FROM cart_items WHERE cartId = #{cartId}")
    int deleteByCartId(Long cartId);
    
    // 根据购物车ID和商品ID删除购物车项
    @Delete("DELETE FROM cart_items WHERE cartId = #{cartId} AND productId = #{productId}")
    int deleteByCartIdAndProductId(Long cartId, Long productId);
    
    // 查询购物车项总数
    @Select("SELECT COUNT(*) FROM cart_items WHERE cartId = #{cartId}")
    int countByCartId(Long cartId);
    
    // 查询购物车商品总数量
    @Select("SELECT SUM(quantity) FROM cart_items WHERE cartId = #{cartId}")
    Integer getTotalQuantityByCartId(Long cartId);
    
    // 查询购物车总金额
    @Select("SELECT SUM(price * quantity) FROM cart_items WHERE cartId = #{cartId}")
    Double getTotalAmountByCartId(Long cartId);
}