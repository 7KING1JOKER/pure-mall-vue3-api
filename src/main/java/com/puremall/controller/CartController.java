package com.puremall.controller;

/**
 * 购物车管理控制器
 * 处理购物车的增删改查等操作
 */

import com.puremall.entity.Cart;
import com.puremall.entity.CartItem;
import com.puremall.service.CartService;
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
    private CartService cartService;

    @GetMapping("/")
    @Operation(summary = "获取购物车及商品列表")
    public Response<Map<String, Object>> getCart(@RequestParam Long userId) {
        Cart cart = cartService.getCart(userId);
        List<CartItem> items = cartService.getCartItems(cart.getId());
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("cart", cart);
        result.put("items", items);
        result.put("statistics", statistics);
        
        return Response.success(result);
    }

    @PostMapping("/")
    @Operation(summary = "添加商品到购物车")
    public Response<Map<String, Object>> addToCart(@RequestParam Long userId, @RequestBody CartItem cartItem) {
        cartService.addToCart(userId, cartItem);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }

    @PutMapping("/item/{cartItemId}/quantity")
    @Operation(summary = "修改购物车项数量")
    public Response<Map<String, Object>> updateCartItemQuantity(
            @RequestParam Long userId, 
            @PathVariable Long cartItemId, 
            @RequestParam Integer quantity) {
        cartService.updateCartItemQuantity(userId, cartItemId, quantity);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }

    @PutMapping("/item/{cartItemId}/selected")
    @Operation(summary = "切换购物车项选中状态")
    public Response<Map<String, Object>> toggleCartItemSelected(
            @RequestParam Long userId, 
            @PathVariable Long cartItemId) {
        cartService.toggleCartItemSelected(userId, cartItemId);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }

    @PutMapping("/selected")
    @Operation(summary = "切换所有购物车项选中状态")
    public Response<Map<String, Object>> toggleAllCartItemsSelected(
            @RequestParam Long userId, 
            @RequestParam Boolean selected) {
        cartService.toggleAllCartItemsSelected(userId, selected);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }

    @DeleteMapping("/item/{cartItemId}")
    @Operation(summary = "删除购物车项")
    public Response<Map<String, Object>> deleteCartItem(
            @RequestParam Long userId, 
            @PathVariable Long cartItemId) {
        cartService.deleteCartItem(userId, cartItemId);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }
    
    @GetMapping("/statistics")
    @Operation(summary = "获取购物车统计信息")
    public Response<Map<String, Object>> getCartStatistics(@RequestParam Long userId) {
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }
    
    @GetMapping("/selected")
    @Operation(summary = "获取选中的购物车商品")
    public Response<List<CartItem>> getSelectedCartItems(@RequestParam Long userId) {
        List<CartItem> selectedItems = cartService.getSelectedCartItems(userId);
        return Response.success(selectedItems);
    }
    
    @DeleteMapping("/selected")
    @Operation(summary = "删除选中的购物车商品")
    public Response<Map<String, Object>> deleteSelectedCartItems(@RequestParam Long userId) {
        cartService.deleteSelectedCartItems(userId);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }
    
    @DeleteMapping("/")
    @Operation(summary = "清空购物车")
    public Response<Void> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return Response.success(null);
    }
    
    @PutMapping("/batch")
    @Operation(summary = "批量更新购物车商品")
    public Response<Map<String, Object>> updateCartItemsBatch(
            @RequestParam Long userId, 
            @RequestBody List<Map<String, Object>> updates) {
        cartService.updateCartItemsBatch(userId, updates);
        // 返回更新后的购物车统计信息
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        return Response.success(statistics);
    }
    
    @GetMapping("/count")
    @Operation(summary = "获取购物车商品总数")
    public Response<Integer> getCartItemCount(@RequestParam Long userId) {
        Map<String, Object> statistics = cartService.getCartStatistics(userId);
        Integer totalCount = (Integer) statistics.get("totalCount");
        return Response.success(totalCount);
    }
}