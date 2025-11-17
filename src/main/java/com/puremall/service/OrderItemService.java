package com.puremall.service;

import com.puremall.entity.OrderItem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface OrderItemService extends IService<OrderItem> {
    List<OrderItem> getOrderItemsByOrderId(Long orderId);
}