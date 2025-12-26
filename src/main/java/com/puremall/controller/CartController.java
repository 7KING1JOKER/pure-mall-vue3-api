package com.puremall.controller;

/**
 * 购物车管理控制器
 * 处理购物车的增删改查等操作
 */

import com.puremall.entity.CartItem;
import com.puremall.service.CartService;
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
@RequestMapping("/api/cart")
@Tag(name = "购物车管理", description = "购物车增删改查接口")
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @GetMapping("/getCartItems")
    @Operation(summary = "获取购物车及商品列表")
    public Response<Map<String, Object>> getCartItems(@RequestParam String username) {
        Long userId = userService.getUserIdByUsername(username);
        List<CartItem> items = cartService.getCartItems(userId);
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("cartItems", items);
        result.put("statistics", statistics);
        
        return Response.success(result);
    }

    @PostMapping("/addCartItems")
    @Operation(summary = "添加商品到购物车")
    public Response<Map<String, Object>> addCartItems(@RequestParam String username, @RequestBody CartItem cartItem) {
        Long userId = userService.getUserIdByUsername(username);
        cartService.addCartItems(userId, cartItem);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }

    @DeleteMapping("/deleteCartItem")
    @Operation(summary = "删除购物车项")
    public Response<Map<String, Object>> deleteCartItemById(
            @RequestParam String username, 
            @RequestParam Long Id) {
        Long userId = userService.getUserIdByUsername(username);
        cartService.deleteCartItemById(Id);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }

    @DeleteMapping("/deleteSelectedCartItems")
    @Operation(summary = "删除选中的购物车商品")
    public Response<Map<String, Object>> deleteSelectedCartItems(@RequestParam Long userId) {
        cartService.deleteSelectedCartItems(userId);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }

    @DeleteMapping("/clearCartItems")
    @Operation(summary = "清空购物车")
    public Response<Void> clearCartItems(@RequestParam String username) {
        Long userId = userService.getUserIdByUsername(username);
        cartService.clearCartItems(userId);
        return Response.success(null);
    }


    @PutMapping("/updateCartItemQuantity")
    @Operation(summary = "修改购物车项数量")
    public Response<Map<String, Object>> updateCartItemQuantity(
            @RequestParam Long userId, 
            @RequestParam Long cartItemId, 
            @RequestParam Integer quantity) {
        
        cartService.updateCartItemQuantity(userId, cartItemId, quantity);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }

    @PutMapping("/selectedCartItem")
    @Operation(summary = "切换购物车项选中状态")
    public Response<Map<String, Object>> toggleCartItemSelected(
            @RequestParam Long userId, 
            @RequestParam Long cartItemId) {
        cartService.toggleCartItemSelected(userId, cartItemId);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }

    @PutMapping("/selectedAll")
    @Operation(summary = "切换所有购物车项选中状态")
    public Response<Map<String, Object>> toggleAllCartItemsSelected(
            @RequestParam Long userId, 
            @RequestParam Boolean selected) {
        cartService.toggleAllCartItemsSelected(userId, selected);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }
    
    @GetMapping("/statistics")
    @Operation(summary = "获取购物车统计信息")
    public Response<Map<String, Object>> getCartStatistics(@RequestParam String username) {
        Long userId = userService.getUserIdByUsername(username);
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }
    
    @GetMapping("/selected")
    @Operation(summary = "获取选中的购物车商品")
    public Response<List<CartItem>> getSelectedCartItems(@RequestParam String username) {
        Long userId = userService.getUserIdByUsername(username);
        List<CartItem> selectedItems = cartService.getSelectedCartItems(userId);
        return Response.success(selectedItems);
    }
    
}