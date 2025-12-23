package com.puremall.service;

import com.puremall.entity.OrderItem;
import java.util.List;
import java.util.Map;

/**
 * 订单商品项Service接口
 * 提供订单商品项的业务逻辑操作方法
 */
public interface OrderItemService {
    
    Map<String, Object> addOrderItem(Long userId, OrderItem orderItem);
    
    Map<String, Object> addOrderItems(Long userId, List<OrderItem> orderItems);
    
    Map<String, Object> getOrderItem(Long userId, String orderNumber);
    Map<String, Object> deleteOrderItem(Long userId, String orderNumber);
}