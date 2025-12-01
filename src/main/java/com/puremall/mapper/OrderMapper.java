package com.puremall.mapper;

/**
 * 订单Mapper接口
 * 用于订单数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.Order;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import java.util.Map;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    // 根据用户ID查询订单
    @Select("SELECT * FROM orders WHERE userId = #{userId} ORDER BY createTime DESC")
    List<Order> findByUserId(@Param("userId") Long userId);
    
    // 根据订单号查询订单
    @Select("SELECT * FROM orders WHERE orderNumber = #{orderNumber}")
    Order findByOrderNumber(@Param("orderNumber") String orderNumber);
    
    // 根据ID查询订单
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Order findById(@Param("id") Long id);
    
    // 查询所有订单
    @Select("SELECT * FROM orders ORDER BY createTime DESC")
    List<Order> findAll();
    
    // 根据状态查询订单
    @Select("SELECT * FROM orders WHERE status = #{status} ORDER BY createTime DESC")
    List<Order> findByStatus(@Param("status") String status);
    
    // 根据用户ID和状态查询订单
    @Select("SELECT * FROM orders WHERE userId = #{userId} AND status = #{status} ORDER BY createTime DESC")
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
    
    // 插入订单
    @Insert("INSERT INTO orders(userId, orderNumber, orderTime, paymentTime, deliveryTime, receiveTime, orderAmount, paymentMethod, status, receiverName, receiverPhone, receiverAddress, remark, createTime, updateTime) VALUES(#{userId}, #{orderNumber}, #{orderTime}, #{paymentTime}, #{deliveryTime}, #{receiveTime}, #{orderAmount}, #{paymentMethod}, #{status}, #{receiverName}, #{receiverPhone}, #{receiverAddress}, #{remark}, NOW(), NOW())")
    int insert(Order order);
    
    // 更新订单
    @Update("UPDATE orders SET orderNumber = #{orderNumber}, orderTime = #{orderTime}, paymentTime = #{paymentTime}, deliveryTime = #{deliveryTime}, receiveTime = #{receiveTime}, orderAmount = #{orderAmount}, paymentMethod = #{paymentMethod}, status = #{status}, receiverName = #{receiverName}, receiverPhone = #{receiverPhone}, receiverAddress = #{receiverAddress}, remark = #{remark}, updateTime = NOW() WHERE id = #{id}")
    int update(Order order);
    
    // 更新订单状态
    @Update("UPDATE orders SET status = #{status}, updateTime = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    // 更新订单支付方式
    @Update("UPDATE orders SET paymentMethod = #{paymentMethod}, updateTime = NOW() WHERE id = #{id}")
    int updatePaymentMethod(@Param("id") Long id, @Param("paymentMethod") String paymentMethod);
    
    // 根据ID删除订单
    @Delete("DELETE FROM orders WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
    
    // 根据用户ID删除订单
    @Delete("DELETE FROM orders WHERE userId = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);
    
    // 查询订单总数
    @Select("SELECT COUNT(*) FROM orders")
    int countAll();
    
    // 查询用户订单总数
    @Select("SELECT COUNT(*) FROM orders WHERE userId = #{userId}")
    int countByUserId(@Param("userId") Long userId);
    
    // 查询指定状态的订单数量
    @Select("SELECT COUNT(*) FROM orders WHERE status = #{status}")
    int countByStatus(@Param("status") String status);
    
    // 查询订单统计信息（按状态分组）
    @Select("SELECT status, COUNT(*) as count FROM orders GROUP BY status")
    List<Object> countByStatusGroup();
    
    // 查找最近的订单
    @Select("SELECT * FROM orders ORDER BY createTime DESC LIMIT #{limit}")
    List<Order> findRecentOrders(@Param("limit") Integer limit);
    
    // 查询用户的订单列表（包含订单项信息）
    @Select("SELECT o.*, oi.name, oi.price, oi.quantity FROM orders o JOIN order_items oi ON o.id = oi.orderId WHERE o.userId = #{userId} ORDER BY o.updateTime DESC")
    List<Map<String, Object>> findOrdersWithItemsByUserId(@Param("userId") Long userId);
    
    // 查询订单详情（包含订单项）
    @Select("SELECT o.*, oi.* FROM orders o JOIN order_items oi ON o.id = oi.orderId WHERE o.id = #{orderId}")
    List<Map<String, Object>> findOrderDetailWithItems(@Param("orderId") Long orderId);
}