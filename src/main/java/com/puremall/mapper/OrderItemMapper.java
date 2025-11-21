package com.puremall.mapper;

/**
 * 订单项Mapper接口
 * 用于订单项数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.OrderItem;
import java.util.List;

public interface OrderItemMapper extends BaseMapper<OrderItem> {
    // 自定义查询方法
    List<OrderItem> findByOrderId(Long orderId);
}