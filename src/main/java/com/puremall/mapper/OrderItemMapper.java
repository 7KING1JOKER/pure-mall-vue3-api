package com.puremall.mapper;

/**
 * 订单项Mapper接口
 * 用于订单项数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.OrderItem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    // 根据订单ID查询订单项列表
    @Select("SELECT * FROM order_items WHERE orderId = #{orderId} ORDER BY id ASC")
    List<OrderItem> findByOrderId(Long orderId);
    
    // 根据ID查询订单项
    @Select("SELECT * FROM order_items WHERE id = #{id}")
    OrderItem findById(Long id);
    
    // 根据商品ID查询订单项
    @Select("SELECT * FROM order_items WHERE productId = #{productId} ORDER BY createTime DESC")
    List<OrderItem> findByProductId(Long productId);
    
    // 根据用户ID查询订单项（通过关联查询）
    @Select("SELECT oi.* FROM order_items oi JOIN orders o ON oi.orderId = o.id WHERE o.userId = #{userId} ORDER BY oi.createTime DESC")
    List<OrderItem> findByUserId(Long userId);
    
    // 插入订单项
    @Insert("INSERT INTO order_items(orderId, productId, specId, name, price, quantity, imageUrl, selected, createTime) VALUES(#{orderId}, #{productId}, #{specId}, #{name}, #{price}, #{quantity}, #{imageUrl}, #{selected}, NOW())")
    int insert(OrderItem orderItem);
    
    // 批量插入订单项
    @Insert("<script>INSERT INTO order_items(orderId, productId, specId, name, price, quantity, imageUrl, selected, createTime) VALUES <foreach item='item' collection='list' separator=','>(#{item.orderId}, #{item.productId}, #{item.specId}, #{item.name}, #{item.price}, #{item.quantity}, #{item.imageUrl}, #{item.selected}, NOW())</foreach></script>")
    int batchInsert(List<OrderItem> orderItems);
    
    // 更新订单项
    @Update("UPDATE order_items SET name = #{name}, price = #{price}, quantity = #{quantity}, imageUrl = #{imageUrl}, selected = #{selected} WHERE id = #{id}")
    int update(OrderItem orderItem);
    
    // 根据ID删除订单项
    @Delete("DELETE FROM order_items WHERE id = #{id}")
    int deleteById(Long id);
    
    // 根据订单ID删除所有订单项
    @Delete("DELETE FROM order_items WHERE orderId = #{orderId}")
    int deleteByOrderId(Long orderId);
    
    // 根据商品ID删除所有订单项
    @Delete("DELETE FROM order_items WHERE productId = #{productId}")
    int deleteByProductId(Long productId);
    
    // 查询订单项总数
    @Select("SELECT COUNT(*) FROM order_items WHERE orderId = #{orderId}")
    int countByOrderId(Long orderId);
    
    // 查询订单商品总数量
    @Select("SELECT SUM(quantity) FROM order_items WHERE orderId = #{orderId}")
    Integer getTotalQuantityByOrderId(Long orderId);
    
    // 查询商品销售数量
    @Select("SELECT SUM(quantity) FROM order_items WHERE productId = #{productId}")
    Integer getSalesCountByProductId(Long productId);
    
    // 查询热销商品（按销量排序）
    @Select("SELECT productId, SUM(quantity) as salesCount FROM order_items GROUP BY productId ORDER BY salesCount DESC LIMIT #{limit}")
    List<Object> findHotProducts(Integer limit);
}