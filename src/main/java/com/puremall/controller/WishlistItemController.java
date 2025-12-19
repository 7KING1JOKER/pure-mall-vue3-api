package com.puremall.controller;

/**
 * 收藏夹商品项管理控制器
 * 处理收藏夹商品项的增删改查等操作
 */

import com.puremall.entity.Product;
import com.puremall.service.WishlistItemService;
import com.puremall.service.UserService;
import com.puremall.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "收藏夹商品项管理", description = "收藏夹商品项增删改查接口")
public class WishlistItemController {

    @Autowired
    private UserService userService;

    @Autowired
    private WishlistItemService wishlistItemService;

    @GetMapping("/getWishlistItems")
    @Operation(summary = "获取用户收藏夹商品列表")
    public Response<List<Product>> getWishlistItems(@RequestParam String username) {
        Long userId = userService.getUserIdByUsername(username);
        List<Product> items = wishlistItemService.getWishlistItemsByUserId(userId);
        return Response.success(items);
    }

    @PostMapping("/addWishlistItem")
    @Operation(summary = "添加商品到收藏夹")
    public Response<Void> addToWishlist(@RequestParam String username, @RequestParam Long productId) {
        Long userId = userService.getUserIdByUsername(username);
        wishlistItemService.addToWishlist(userId, productId);
        return Response.success(null);
    }

    @DeleteMapping("/removeWishlistItem")
    @Operation(summary = "从收藏夹删除商品")
    public Response<Void> removeFromWishlist(@RequestParam String username, @RequestParam Long productId) {
        Long userId = userService.getUserIdByUsername(username);
        wishlistItemService.removeFromWishlist(userId, productId);
        return Response.success(null);
    }

    @GetMapping("/checkInWishlistItem")
    @Operation(summary = "检查商品是否在收藏夹中")
    public Response<Boolean> isInWishlist(@RequestParam String username, @RequestParam Long productId) {
        Long userId = userService.getUserIdByUsername(username);
        boolean isInWishlist = wishlistItemService.isInWishlist(userId, productId);
        return Response.success(isInWishlist);
    }
}
