package com.puremall.service.impl;

/**
 * 订单服务实现类
 * 实现订单相关的业务逻辑操作
 */

import com.puremall.entity.Order;
import com.puremall.entity.OrderItem;
import com.puremall.entity.ProductSpec;
import com.puremall.mapper.OrderMapper;
import com.puremall.mapper.OrderItemMapper;
import com.puremall.mapper.ProductSpecMapper;
import com.puremall.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puremall.exception.BusinessException;
import com.puremall.utils.OrderNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private ProductSpecMapper productSpecMapper;

    @Override
    public Map<String, Object> getOrdersByUserId(Long userId, Integer status, Integer page, Integer pageSize) {
        Page<Order> orderPage = new Page<>(page, pageSize);
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        
        Page<Order> result = orderMapper.selectPage(orderPage, wrapper);
        List<Order> orders = result.getRecords();
        
        // 为每个订单加载订单项
        for (Order order : orders) {
            loadOrderItems(order);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("list", orders);
        response.put("total", result.getTotal());
        response.put("page", page);
        response.put("pageSize", pageSize);
        
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
    
    private void loadOrderItems(Order order) {
        if (order != null) {
            QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
            wrapper.eq("order_id", order.getId());
            List<OrderItem> orderItems = orderItemMapper.selectList(wrapper);
            order.setOrderItems(orderItems);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> createOrder(Long userId, Map<String, Object> orderData) {
          // 验证数据
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> itemsData = (List<Map<String, Object>>) orderData.get("items");
        if (itemsData == null || itemsData.isEmpty()) {
            throw new BusinessException("订单商品不能为空");
        }
        
        // 创建订单对象
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNumber(OrderNumberUtils.generateOrderNumber());
        order.setStatus("pending"); // 初始状态为待支付
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        
        // 设置收货信息
        // Order类没有setAddressId方法，使用setReceiverAddress代替
        if (orderData.containsKey("address")) {
            order.setReceiverAddress((String) orderData.get("address"));
        }
        if (orderData.containsKey("receiverName")) {
            order.setReceiverName((String) orderData.get("receiverName"));
        }
        if (orderData.containsKey("receiverPhone")) {
            order.setReceiverPhone((String) orderData.get("receiverPhone"));
        }
        
        // 计算订单总金额并检查库存
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<Long, ProductSpec> specCache = new HashMap<>();
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (Map<String, Object> itemData : itemsData) {
            OrderItem item = new OrderItem();
            // 直接进行类型转换，避免不必要的中间变量
             item.setProductId(Long.valueOf(itemData.get("productId").toString()));
             item.setSpecId(Long.valueOf(itemData.get("specId").toString()));
             item.setQuantity(Integer.valueOf(itemData.get("quantity").toString()));
             item.setName((String) itemData.get("name"));
             item.setImageUrl((String) itemData.get("imageUrl"));
            
            // 获取商品规格
            ProductSpec spec = productSpecMapper.selectById(item.getSpecId());
            if (spec == null) {
                throw new BusinessException("商品规格不存在");
            }
            
            // 检查库存
            if (item.getQuantity() > spec.getStock()) {
                throw new BusinessException("商品库存不足");
            }
            
            // 保存规格到缓存，用于后续更新库存
            specCache.put(item.getSpecId(), spec);
            
            // 设置商品价格和小计
            item.setPrice(spec.getPrice());
            orderItems.add(item);
        }
        
        // 保存订单
        orderMapper.insert(order);
        
        // 保存订单项并更新库存
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            // OrderItem类没有setCreateTime方法，直接保存
            orderItemMapper.insert(item);
            
            // 更新库存
            ProductSpec spec = specCache.get(item.getSpecId());
            spec.setStock(spec.getStock() - item.getQuantity());
            spec.setSalesAmount(spec.getSalesAmount() + item.getQuantity());
            productSpecMapper.updateById(spec);
            
            // 累加总金额
            totalAmount = totalAmount.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        
        // 更新订单总金额
        order.setOrderAmount(totalAmount); // 使用正确的setter方法
        orderMapper.updateById(order);
        
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getId());
        result.put("orderNumber", order.getOrderNumber());
        result.put("totalAmount", totalAmount);
        
        return result;
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
    @Transactional
    public Map<String, Object> cancelOrder(Long userId, String orderNumber, String reason) {
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
        
        // 加载订单项以恢复库存
        loadOrderItems(order);
        List<OrderItem> orderItems = order.getOrderItems();
        
        // 恢复库存
        for (OrderItem item : orderItems) {
            ProductSpec spec = productSpecMapper.selectById(item.getSpecId());
            if (spec != null) {
                spec.setStock(spec.getStock() + item.getQuantity());
                spec.setSalesAmount(Math.max(0, spec.getSalesAmount() - item.getQuantity()));
                productSpecMapper.updateById(spec);
            }
        }
        
        // 更新订单状态为已取消
        order.setStatus("cancelled");
        // Order类没有setCancelReason和setCancelTime方法，使用remark存储取消原因
        order.setRemark(reason);
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "订单已取消");
        
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
    public List<OrderItem> getOrderItemsByOrderNumber(String orderNumber) {
        Order order = orderMapper.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", order.getId());
        return orderItemMapper.selectList(wrapper);
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
        wrapper.eq("user_id", userId);
        
        // 待支付订单
        wrapper.eq("status", "pending");
        statusCount.put("pending", Math.toIntExact(orderMapper.selectCount(wrapper)));
        
        // 已支付订单
        wrapper.setEntity(new Order()); // 重置条件
        wrapper.eq("user_id", userId);
        wrapper.eq("status", "paid");
        statusCount.put("paid", Math.toIntExact(orderMapper.selectCount(wrapper)));
        
        // 已发货订单
        wrapper.setEntity(new Order());
        wrapper.eq("user_id", userId);
        wrapper.eq("status", "shipped");
        statusCount.put("shipped", Math.toIntExact(orderMapper.selectCount(wrapper)));
        
        // 已完成订单
        wrapper.setEntity(new Order());
        wrapper.eq("user_id", userId);
        wrapper.eq("status", "completed");
        statusCount.put("completed", Math.toIntExact(orderMapper.selectCount(wrapper)));
        
        // 已取消订单
        wrapper.setEntity(new Order());
        wrapper.eq("user_id", userId);
        wrapper.eq("status", "cancelled");
        statusCount.put("cancelled", Math.toIntExact(orderMapper.selectCount(wrapper)));
        
        return statusCount;
    }
    

}