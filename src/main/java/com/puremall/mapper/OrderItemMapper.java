package com.puremall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.OrderItem;
import java.util.List;

public interface OrderItemMapper extends BaseMapper<OrderItem> {
    // 自定义查询方法
    List<OrderItem> findByOrderId(Long orderId);
}