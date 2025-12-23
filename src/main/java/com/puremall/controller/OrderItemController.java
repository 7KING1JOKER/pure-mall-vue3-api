package com.puremall.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.puremall.entity.OrderItem;
import com.puremall.service.OrderItemService;
import com.puremall.response.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orderItem")
@Tag(name = "订单商品管理", description = "订单商品的创建、查询、更新等操作")

public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;
    
    @PostMapping("/addOrderItem")
    @Operation(summary = "批量添加订单商品")
    public Response<Map<String, Object>> addOrderItem(@RequestParam Long userId, @RequestBody List<OrderItem> orderItems) {
        Map<String, Object> result = orderItemService.addOrderItems(userId, orderItems);
        return Response.success(result);
    }

    @GetMapping("/getOrderItem")
    @Operation(summary = "查询订单商品")
    public Response<Map<String, Object>> getOrderItem(@RequestParam Long userId, @RequestParam String orderNumber) {
        Map<String, Object> result = orderItemService.getOrderItem(userId, orderNumber);
        return Response.success(result);
    }

    @DeleteMapping("/deleteOrderItem")
    @Operation(summary = "删除订单商品")
    public Response<Map<String, Object>> deleteOrderItem(@RequestParam Long userId, @RequestParam String orderNumber) {
        Map<String, Object> result = orderItemService.deleteOrderItem(userId, orderNumber);
        return Response.success(result);
    }

}
