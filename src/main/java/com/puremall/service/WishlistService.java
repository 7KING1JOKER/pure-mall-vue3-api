package com.puremall.service;

/**
 * 收藏夹服务接口
 * 提供收藏夹相关的业务逻辑操作
 */

import com.puremall.entity.Wishlist;
import com.puremall.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface WishlistService extends IService<Wishlist> {
    Wishlist getWishlist(Long userId);
    List<Product> getWishlistItems(Long wishlistId);
    void addToWishlist(Long userId, Long productId);
    void removeFromWishlist(Long userId, Long productId);
    boolean isInWishlist(Long userId, Long productId);
}