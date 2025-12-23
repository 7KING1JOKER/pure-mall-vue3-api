package com.puremall.mapper;

/**
 * 订单商品项Mapper接口
 * 提供订单商品项的数据库操作方法
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.OrderItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    
    // 根据订单号查询所有订单项
    @Select("SELECT * FROM order_items WHERE orderNumber = #{orderNumber} AND userId = #{userId}")
    List<OrderItem> findByOrderNumberAndUserId(@Param("orderNumber") String orderNumber, @Param("userId") Long userId);
    
    // 根据订单项ID查询订单项
    @Select("SELECT * FROM order_items WHERE id = #{id}")
    OrderItem findById(@Param("id") Long id);
        
    // 根据订单号删除订单项
    @Delete("DELETE FROM order_items WHERE orderNumber = #{orderNumber}")
    int deleteByOrderNumber(@Param("orderNumber") String orderNumber);
}