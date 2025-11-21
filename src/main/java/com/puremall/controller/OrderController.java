package com.puremall.controller;

/**
 * 订单管理控制器
 * 处理订单的创建、查询、更新等操作
 */

import com.puremall.entity.Order;
import com.puremall.service.OrderService;
import com.puremall.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@Tag(name = "订单管理", description = "订单创建、查询、支付接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    @Operation(summary = "获取用户订单列表")
    public Response<List<Order>> getOrders(@RequestParam Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return Response.success(orders);
    }

    @GetMapping("/{orderNumber}")
    @Operation(summary = "获取订单详情")
    public Response<Order> getOrderDetail(@RequestParam Long userId, @PathVariable String orderNumber) {
        Order order = orderService.getOrderByOrderNumber(userId, orderNumber);
        return Response.success(order);
    }

    @PostMapping("/")
    @Operation(summary = "创建订单")
    public Response<Order> createOrder(@RequestParam Long userId, @RequestBody Order order) {
        Order newOrder = orderService.createOrder(userId, order);
        return Response.success(newOrder);
    }

    @PutMapping("/{orderNumber}/pay")
    @Operation(summary = "支付订单")
    public Response<Void> payOrder(@RequestParam Long userId, @PathVariable String orderNumber) {
        orderService.payOrder(userId, orderNumber);
        return Response.success(null);
    }

    @PutMapping("/{orderNumber}/cancel")
    @Operation(summary = "取消订单")
    public Response<Void> cancelOrder(@RequestParam Long userId, @PathVariable String orderNumber) {
        orderService.cancelOrder(userId, orderNumber);
        return Response.success(null);
    }

    @PutMapping("/{orderNumber}/confirm")
    @Operation(summary = "确认收货")
    public Response<Void> confirmReceive(@RequestParam Long userId, @PathVariable String orderNumber) {
        orderService.confirmReceive(userId, orderNumber);
        return Response.success(null);
    }
}