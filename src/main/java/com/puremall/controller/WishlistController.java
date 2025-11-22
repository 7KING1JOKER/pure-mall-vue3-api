package com.puremall.controller;

/**
 * 收藏夹管理控制器
 * 处理收藏夹的增删改查等操作
 */

import com.puremall.entity.Wishlist;
import com.puremall.entity.Product;
import com.puremall.service.WishlistService;
import com.puremall.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "收藏夹管理", description = "收藏夹增删改查接口")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/")
    @Operation(summary = "获取收藏夹")
    public Response<Map<String, Object>> getWishlist(@RequestParam Long userId) {
        Wishlist wishlist = wishlistService.getWishlist(userId);
        List<Product> items = wishlistService.getWishlistItems(wishlist.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("wishlist", wishlist);
        result.put("items", items);
        
        return Response.success(result);
    }

    @PostMapping("/")
    @Operation(summary = "添加商品到收藏夹")
    public Response<Void> addToWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        wishlistService.addToWishlist(userId, productId);
        return Response.success(null);
    }

    @DeleteMapping("/item")
    @Operation(summary = "从收藏夹删除商品")
    public Response<Void> removeFromWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        wishlistService.removeFromWishlist(userId, productId);
        return Response.success(null);
    }

    @GetMapping("/check")
    @Operation(summary = "检查商品是否在收藏夹中")
    public Response<Boolean> isInWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        boolean isInWishlist = wishlistService.isInWishlist(userId, productId);
        return Response.success(isInWishlist);
    }
}