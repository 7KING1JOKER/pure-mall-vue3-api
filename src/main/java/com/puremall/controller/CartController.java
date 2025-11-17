package com.puremall.controller;

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
    @Operation(summary = "获取购物车")
    public Response<Map<String, Object>> getCart(@RequestParam Long userId) {
        Cart cart = cartService.getCart(userId);
        List<CartItem> items = cartService.getCartItems(cart.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("cart", cart);
        result.put("items", items);
        
        return Response.success(result);
    }

    @PostMapping("/")
    @Operation(summary = "添加商品到购物车")
    public Response<Void> addToCart(@RequestParam Long userId, @RequestBody CartItem cartItem) {
        cartService.addToCart(userId, cartItem);
        return Response.success(null);
    }

    @PutMapping("/item/{cartItemId}/quantity")
    @Operation(summary = "修改购物车项数量")
    public Response<Void> updateCartItemQuantity(@RequestParam Long userId, @PathVariable Long cartItemId, @RequestParam Integer quantity) {
        cartService.updateCartItemQuantity(userId, cartItemId, quantity);
        return Response.success(null);
    }

    @PutMapping("/item/{cartItemId}/selected")
    @Operation(summary = "切换购物车项选中状态")
    public Response<Void> toggleCartItemSelected(@RequestParam Long userId, @PathVariable Long cartItemId) {
        cartService.toggleCartItemSelected(userId, cartItemId);
        return Response.success(null);
    }

    @PutMapping("/selected")
    @Operation(summary = "切换所有购物车项选中状态")
    public Response<Void> toggleAllCartItemsSelected(@RequestParam Long userId, @RequestParam Boolean selected) {
        cartService.toggleAllCartItemsSelected(userId, selected);
        return Response.success(null);
    }

    @DeleteMapping("/item/{cartItemId}")
    @Operation(summary = "删除购物车项")
    public Response<Void> deleteCartItem(@RequestParam Long userId, @PathVariable Long cartItemId) {
        cartService.deleteCartItem(userId, cartItemId);
        return Response.success(null);
    }
}