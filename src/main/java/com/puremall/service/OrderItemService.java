package com.puremall.service;

/**
 * 订单项服务接口
 * 提供订单项相关的业务逻辑操作
 */

import com.puremall.entity.OrderItem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface OrderItemService extends IService<OrderItem> {
    List<OrderItem> getOrderItemsByOrderId(Long orderId);
}