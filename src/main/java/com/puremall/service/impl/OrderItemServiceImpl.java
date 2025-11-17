package com.puremall.service.impl;

import com.puremall.entity.OrderItem;
import com.puremall.mapper.OrderItemMapper;
import com.puremall.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemMapper.findByOrderId(orderId);
    }
}