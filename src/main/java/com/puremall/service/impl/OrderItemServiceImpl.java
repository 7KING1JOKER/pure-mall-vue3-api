package com.puremall.service.impl;

/**
 * 订单商品项Service实现类
 * 实现订单商品项的业务逻辑操作
 */

import com.puremall.entity.OrderItem;
import com.puremall.mapper.OrderItemMapper;
import com.puremall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public Map<String, Object> addOrderItem(Long userId, OrderItem orderItem) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 验证订单商品项信息
            if (orderItem == null) {
                result.put("success", false);
                result.put("message", "订单商品项信息不能为空");
                return result;
            }

            if (orderItem.getOrderNumber() == null || orderItem.getOrderNumber().isEmpty()) {
                result.put("success", false);
                result.put("message", "订单号不能为空");
                return result;
            }

            if (orderItem.getName() == null || orderItem.getName().isEmpty()) {
                result.put("success", false);
                result.put("message", "商品名称不能为空");
                return result;
            }

            if (orderItem.getQuantity() == null || orderItem.getQuantity() <= 0) {
                result.put("success", false);
                result.put("message", "商品数量必须大于0");
                return result;
            }

            if (orderItem.getPrice() == null || orderItem.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                result.put("success", false);
                result.put("message", "商品价格必须大于0");
                return result;
            }
            
            // 设置用户ID
            orderItem.setUserId(userId);

            // 保存订单商品项
            int count = orderItemMapper.insert(orderItem);
            if (count > 0) {
                result.put("success", true);
                result.put("message", "订单商品项添加成功");
                result.put("orderItem", orderItem);
            } else {
                result.put("success", false);
                result.put("message", "订单商品项添加失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "订单商品项添加失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
    
    @Override
    public Map<String, Object> addOrderItems(Long userId, List<OrderItem> orderItems) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证订单商品项列表
            if (orderItems == null || orderItems.isEmpty()) {
                result.put("success", false);
                result.put("message", "订单商品项列表不能为空");
                return result;
            }
            
            // 验证每个订单商品项
            for (OrderItem orderItem : orderItems) {
                if (orderItem.getOrderNumber() == null || orderItem.getOrderNumber().isEmpty()) {
                    result.put("success", false);
                    result.put("message", "订单号不能为空");
                    return result;
                }

                if (orderItem.getName() == null || orderItem.getName().isEmpty()) {
                    result.put("success", false);
                    result.put("message", "商品名称不能为空");
                    return result;
                }

                if (orderItem.getQuantity() == null || orderItem.getQuantity() <= 0) {
                    result.put("success", false);
                    result.put("message", "商品数量必须大于0");
                    return result;
                }

                if (orderItem.getPrice() == null || orderItem.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    result.put("success", false);
                    result.put("message", "商品价格必须大于0");
                    return result;
                }
                
                // 设置用户ID
                orderItem.setUserId(userId);
            }
            
            // 批量保存订单商品项
            int successCount = 0;
            for (OrderItem orderItem : orderItems) {
                if (orderItemMapper.insert(orderItem) > 0) {
                    successCount++;
                }
            }
            
            if (successCount > 0) {
                result.put("success", true);
                result.put("message", "订单商品项批量添加成功");
                result.put("successCount", successCount);
                result.put("totalCount", orderItems.size());
            } else {
                result.put("success", false);
                result.put("message", "订单商品项批量添加失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "订单商品项批量添加失败：" + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getOrderItem(Long userId, String orderNumber) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 验证参数
            if (orderNumber == null || orderNumber.isEmpty()) {
                result.put("success", false);
                result.put("message", "订单号不能为空");
                return result;
            }

            // 查询订单商品项
            List<OrderItem> orderItems = orderItemMapper.findByOrderNumberAndUserId(orderNumber, userId);
            if (orderItems == null || orderItems.isEmpty()) {
                result.put("success", false);
                result.put("message", "订单商品项不存在");
                return result;
            }

            // （这里可以添加用户权限验证，确保用户只能查看自己的订单商品项）
            // 由于订单商品项表中没有直接的userId字段，需要通过订单表关联查询
            // 暂时省略权限验证，后续可以根据需要添加

            result.put("success", true);
            result.put("message", "订单商品项查询成功");
            result.put("orderItems", orderItems);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "订单商品项查询失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Map<String, Object> deleteOrderItem(Long userId, String orderNumber) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 验证参数
            if (orderNumber == null || orderNumber.isEmpty()) {
                result.put("success", false);
                result.put("message", "订单号不能为空");
                return result;
            }

            // 查询订单商品项是否存在
            List<OrderItem> orderItems = orderItemMapper.findByOrderNumberAndUserId(orderNumber, userId);
            if (orderItems == null || orderItems.isEmpty()) {
                result.put("success", false);
                result.put("message", "订单商品项不存在");
                return result;
            }

            // （这里可以添加用户权限验证，确保用户只能删除自己的订单商品项）
            // 由于订单商品项表中没有直接的userId字段，需要通过订单表关联查询
            // 暂时省略权限验证，后续可以根据需要添加

            // 删除订单商品项
            int count = orderItemMapper.deleteByOrderNumber(orderNumber);
            if (count > 0) {
                result.put("success", true);
                result.put("message", "订单商品项删除成功");
            } else {
                result.put("success", false);
                result.put("message", "订单商品项删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "订单商品项删除失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}