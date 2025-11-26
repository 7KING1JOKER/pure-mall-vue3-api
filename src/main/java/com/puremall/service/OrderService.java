package com.puremall.service;

/**
 * 订单服务接口
 * 提供订单相关的业务逻辑操作
 */

import com.puremall.entity.Order;
import com.puremall.entity.OrderItem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

public interface OrderService extends IService<Order> {
    Map<String, Object> getOrdersByUserId(Long userId, Integer status, Integer page, Integer pageSize);
    Order getOrderByOrderNumber(Long userId, String orderNumber);
    Map<String, Object> createOrder(Long userId, Map<String, Object> orderData);
    Map<String, Object> payOrder(Long userId, String orderNumber);
    Map<String, Object> cancelOrder(Long userId, String orderNumber, String reason);
    Map<String, Object> confirmReceive(Long userId, String orderNumber);
    List<OrderItem> getOrderItemsByOrderNumber(String orderNumber);
    Map<String, Object> getOrderLogisticsInfo(String orderNumber);
    Map<String, Object> addOrderRemark(Long userId, String orderNumber, String remark);
    Map<String, Integer> getOrderStatusCount(Long userId);
    Map<String, Object> reviewOrderItems(Long userId, String orderNumber, List<Map<String, Object>> reviewData);
}