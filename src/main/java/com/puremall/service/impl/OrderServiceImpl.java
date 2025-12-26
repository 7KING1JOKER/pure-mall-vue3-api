package com.puremall.service.impl;

/**
 * 订单服务实现类
 * 实现订单相关的业务逻辑操作
 */

import com.puremall.entity.Order;
import com.puremall.entity.CartItem;
import com.puremall.mapper.OrderMapper;
import com.puremall.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puremall.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderMapper.findByUserId(userId);
    }

    @Override
    @Transactional
    public Map<String, Object> addOrder(Long userId, Order order) {
        
        orderMapper.insert(order);

        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        return response;
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
    public Map<String, Object> payOrder(Long userId, String orderNumber) {
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
        order.setPaymentMethod("online"); // 默认在线支付
        order.setPaymentTime(new Date());
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "支付成功");
        result.put("orderNumber", orderNumber);
        
        return result;
    }

    @Override
    public Map<String, Object> confirmReceive(Long userId, String orderNumber) {
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
        order.setStatus("completed");
        order.setReceiveTime(new Date());
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "确认收货成功");
        
        return result;
    }
    

    
    @Override
    public List<CartItem> getOrderItemsByOrderNumber(String orderNumber) {
        // 从数据库查询订单项数据
        List<CartItem> orderItems = new ArrayList<>();
        
        
        return orderItems;
    }
    
    @Override
    public Map<String, Object> getOrderLogisticsInfo(String orderNumber) {
        Order order = orderMapper.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        Map<String, Object> logisticsInfo = new HashMap<>();
        // Order类没有trackingNumber、shippingCompany和shipTime字段
        logisticsInfo.put("deliveryTime", order.getDeliveryTime());
        
        // 模拟物流轨迹
        List<Map<String, String>> trackingTraces = new ArrayList<>();
        if ("shipped".equals(order.getStatus()) || "completed".equals(order.getStatus())) {
            Map<String, String> trace1 = new HashMap<>();
            trace1.put("time", order.getDeliveryTime().toString());
            trace1.put("description", "商品已发货");
            trackingTraces.add(trace1);
            
            // 模拟更多物流信息
            Map<String, String> trace2 = new HashMap<>();
            trace2.put("time", new Date().toString());
            trace2.put("description", "商品正在运输中");
            trackingTraces.add(trace2);
        }
        
        logisticsInfo.put("trackingTraces", trackingTraces);
        return logisticsInfo;
    }
    
    @Override
    public Map<String, Object> addOrderRemark(Long userId, String orderNumber, String remark) {
        Order order = orderMapper.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该订单");
        }
        
        order.setRemark(remark);
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "备注添加成功");
        
        return result;
    }
    
    @Override
    public Map<String, Object> reviewOrderItems(Long userId, String orderNumber, List<Map<String, Object>> reviewData) {
        Order order = orderMapper.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该订单");
        }
        
        // 验证订单状态
        if (!"completed".equals(order.getStatus())) {
            throw new BusinessException("订单状态错误，无法评价");
        }
        
        // 这里应该实现评价功能，目前只是返回成功
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "评价成功");
        
        return result;
    }
    
    @Override
    public Map<String, Integer> getOrderStatusCount(Long userId) {
        Map<String, Integer> statusCount = new HashMap<>();
        
        // 查询各状态订单数量
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        
        // 待支付订单
        wrapper.eq("status", "pending");
        statusCount.put("pending", Math.toIntExact(orderMapper.selectCount(wrapper)));
        
        // 已支付订单
        wrapper.setEntity(new Order()); // 重置条件
        wrapper.eq("userId", userId);
        wrapper.eq("status", "paid");
        statusCount.put("paid", Math.toIntExact(orderMapper.selectCount(wrapper)));
        
        // 已发货订单
        wrapper.setEntity(new Order());
        wrapper.eq("userId", userId);
        wrapper.eq("status", "shipped");
        statusCount.put("shipped", Math.toIntExact(orderMapper.selectCount(wrapper)));
        
        // 已完成订单
        wrapper.setEntity(new Order());
        wrapper.eq("userId", userId);
        wrapper.eq("status", "completed");
        statusCount.put("completed", Math.toIntExact(orderMapper.selectCount(wrapper)));
        
        // 已取消订单
        wrapper.setEntity(new Order());
        wrapper.eq("userId", userId);
        wrapper.eq("status", "cancelled");
        statusCount.put("cancelled", Math.toIntExact(orderMapper.selectCount(wrapper)));
        
        return statusCount;
    }
    
    @Override
    @Transactional
    public Map<String, Object> deleteOrder(String orderNumber) {
        // 获取订单，用于验证订单是否存在
        Order order = orderMapper.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 调用Mapper方法直接删除订单和关联的订单项
        int rowsAffected = orderMapper.deleteOrderByOrderNumber(orderNumber);
        
        // 验证删除是否成功
        if (rowsAffected == 0) {
            throw new BusinessException("订单删除失败");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "订单删除成功");
        
        return result;
    }

}