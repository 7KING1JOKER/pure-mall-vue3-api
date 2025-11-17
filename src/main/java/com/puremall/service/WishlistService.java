package com.puremall.service;

import com.puremall.entity.Wishlist;
import com.puremall.entity.WishlistItem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface WishlistService extends IService<Wishlist> {
    Wishlist getWishlist(Long userId);
    List<WishlistItem> getWishlistItems(Long wishlistId);
    void addToWishlist(Long userId, Long productId);
    void removeFromWishlist(Long userId, Long productId);
    boolean isInWishlist(Long userId, Long productId);
}