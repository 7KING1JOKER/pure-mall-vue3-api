package com.puremall.service.impl;

/**
 * 订单服务实现类
 * 实现订单相关的业务逻辑操作
 */

import com.puremall.entity.Order;
import com.puremall.entity.OrderItem;
import com.puremall.mapper.OrderMapper;
import com.puremall.mapper.OrderItemMapper;
import com.puremall.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puremall.exception.BusinessException;
import com.puremall.utils.OrderNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderMapper.findByUserId(userId);
    }

    @Override
    public Order getOrderByOrderNumber(Long userId, String orderNumber) {
        Order order = orderMapper.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权查看该订单");
        }
        return order;
    }

    @Override
    @Transactional
    public Order createOrder(Long userId, Order order) {
        // 生成订单号
        String orderNumber = OrderNumberUtils.generateOrderNumber();
        order.setOrderNumber(orderNumber);
        order.setUserId(userId);
        order.setStatus("pending"); // 初始状态为待支付
        
        // 保存订单
        orderMapper.insert(order);
        
        // 保存订单项
        List<OrderItem> orderItems = order.getOrderItems();
        if (orderItems != null && !orderItems.isEmpty()) {
            for (OrderItem orderItem : orderItems) {
                orderItem.setOrderId(order.getId());
                orderItemMapper.insert(orderItem);
            }
        }
        
        return order;
    }

    @Override
    public void payOrder(Long userId, String orderNumber) {
        // 获取订单
        Order order = orderMapper.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该订单");
        }
        
        // 验证订单状态
        if (!"pending".equals(order.getStatus())) {
            throw new BusinessException("订单状态错误，无法支付");
        }
        
        // 更新订单状态为已支付
        order.setStatus("paid");
        orderMapper.updateById(order);
    }

    @Override
    public void cancelOrder(Long userId, String orderNumber) {
        // 获取订单
        Order order = orderMapper.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该订单");
        }
        
        // 验证订单状态
        if (!"pending".equals(order.getStatus())) {
            throw new BusinessException("订单状态错误，无法取消");
        }
        
        // 更新订单状态为已取消
        order.setStatus("cancelled");
        orderMapper.updateById(order);
    }

    @Override
    public void confirmReceive(Long userId, String orderNumber) {
        // 获取订单
        Order order = orderMapper.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该订单");
        }
        
        // 验证订单状态
        if (!"shipped".equals(order.getStatus())) {
            throw new BusinessException("订单状态错误，无法确认收货");
        }
        
        // 更新订单状态为已完成
        order.setStatus("delivered");
        orderMapper.updateById(order);
    }
}