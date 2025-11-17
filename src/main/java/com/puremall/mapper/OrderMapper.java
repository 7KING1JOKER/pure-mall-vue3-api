package com.puremall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.Order;
import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {
    // 自定义查询方法
    List<Order> findByUserId(Long userId);
    Order findByOrderNumber(String orderNumber);
}