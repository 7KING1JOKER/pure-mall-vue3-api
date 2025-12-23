package com.puremall.controller;

/**
 * 订单管理控制器
 * 处理订单的创建、查询、更新等操作
 */

import com.puremall.entity.Order;
import com.puremall.entity.CartItem;
import com.puremall.service.OrderService;
import com.puremall.service.UserService;
import com.puremall.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@Tag(name = "订单管理", description = "订单创建、查询、支付、物流等全流程接口")
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/userOrders")
    @Operation(summary = "获取用户订单列表")
    public Response<Map<String, Object>> getOrders(
            @RequestParam String username) {
        Long userId = userService.getUserIdByUsername(username);
        List<Order> orders = orderService.getOrdersByUserId(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        return Response.success(result);
    }

    @PostMapping("/addOrder")
    @Operation(summary = "添加订单商品")
    public Response<Map<String, Object>> addOrders(@RequestParam Long userId, @RequestBody Order order) {
        Map<String, Object> result = orderService.addOrder(userId, order);
        return Response.success(result);
    }

    @DeleteMapping("/deleteOrder")
    @Operation(summary = "删除订单")
    public Response<Map<String, Object>> deleteOrder(@RequestParam String orderNumber) {
        Map<String, Object> result = orderService.deleteOrder(orderNumber);
        return Response.success(result);
    }
    
    @GetMapping("/{orderNumber}")
    @Operation(summary = "获取订单详情及商品列表")
    public Response<Map<String, Object>> getOrderDetail(@RequestParam Long userId, @RequestParam String orderNumber) {
        Map<String, Object> result = new HashMap<>();
        Order order = orderService.getOrderByOrderNumber(userId, orderNumber);
        List<CartItem> orderItems = orderService.getOrderItemsByOrderNumber(orderNumber);
        Map<String, Object> logisticsInfo = orderService.getOrderLogisticsInfo(orderNumber);
        
        result.put("order", order);
        result.put("orderItems", orderItems);
        result.put("logisticsInfo", logisticsInfo);
        
        return Response.success(result);
    }

    @PostMapping("/")
    @Operation(summary = "创建订单")
    public Response<Map<String, Object>> createOrder(@RequestParam Long userId, @RequestBody Map<String, Object> orderData) {
        Map<String, Object> result = orderService.createOrder(userId, orderData);
        return Response.success(result);
    }

    @PutMapping("/{orderNumber}/pay")
    @Operation(summary = "支付订单")
    public Response<Map<String, Object>> payOrder(@RequestParam Long userId, @PathVariable String orderNumber) {
        Map<String, Object> paymentResult = orderService.payOrder(userId, orderNumber);
        return Response.success(paymentResult);
    }

    @PutMapping("/{orderNumber}/cancel")
    @Operation(summary = "取消订单")
    public Response<Map<String, Object>> cancelOrder(@RequestParam Long userId, @PathVariable String orderNumber, @RequestParam(required = false) String reason) {
        Map<String, Object> result = orderService.cancelOrder(userId, orderNumber, reason);
        return Response.success(result);
    }

    @PutMapping("/{orderNumber}/confirm")
    @Operation(summary = "确认收货")
    public Response<Map<String, Object>> confirmReceive(@RequestParam Long userId, @PathVariable String orderNumber) {
        Map<String, Object> result = orderService.confirmReceive(userId, orderNumber);
        return Response.success(result);
    }
    
    @GetMapping("/{orderNumber}/items")
    @Operation(summary = "获取订单商品列表")
    public Response<List<CartItem>> getOrderItems(@PathVariable String orderNumber) {
        List<CartItem> orderItems = orderService.getOrderItemsByOrderNumber(orderNumber);
        return Response.success(orderItems);
    }
    
    @GetMapping("/{orderNumber}/logistics")
    @Operation(summary = "获取订单物流信息")
    public Response<Map<String, Object>> getOrderLogistics(@PathVariable String orderNumber) {
        Map<String, Object> logisticsInfo = orderService.getOrderLogisticsInfo(orderNumber);
        return Response.success(logisticsInfo);
    }
    
    @PostMapping("/{orderNumber}/remark")
    @Operation(summary = "添加订单备注")
    public Response<Map<String, Object>> addOrderRemark(@RequestParam Long userId, @PathVariable String orderNumber, @RequestBody Map<String, String> remarkData) {
        String remark = remarkData.get("remark");
        Map<String, Object> result = orderService.addOrderRemark(userId, orderNumber, remark);
        return Response.success(result);
    }
    
    @GetMapping("/status/count")
    @Operation(summary = "获取各状态订单数量统计")
    public Response<Map<String, Integer>> getOrderStatusCount(@RequestParam Long userId) {
        Map<String, Integer> countMap = orderService.getOrderStatusCount(userId);
        return Response.success(countMap);
    }
    
    @PostMapping("/{orderNumber}/review")
    @Operation(summary = "评价订单商品")
    public Response<Map<String, Object>> reviewOrderItems(
            @RequestParam Long userId, 
            @PathVariable String orderNumber,
            @RequestBody List<Map<String, Object>> reviewData) {
        Map<String, Object> result = orderService.reviewOrderItems(userId, orderNumber, reviewData);
        return Response.success(result);
    }
    

}