package com.puremall.service;

import com.puremall.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface OrderService extends IService<Order> {
    List<Order> getOrdersByUserId(Long userId);
    Order getOrderByOrderNumber(Long userId, String orderNumber);
    Order createOrder(Long userId, Order order);
    void payOrder(Long userId, String orderNumber);
    void cancelOrder(Long userId, String orderNumber);
    void confirmReceive(Long userId, String orderNumber);
}