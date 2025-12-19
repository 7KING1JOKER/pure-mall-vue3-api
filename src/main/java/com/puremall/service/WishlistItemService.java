package com.puremall.service;

/**
 * 收藏夹商品项服务接口
 * 提供收藏夹商品项相关的业务逻辑操作
 */

import com.puremall.entity.WishlistItem;
import com.puremall.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface WishlistItemService extends IService<WishlistItem> {

    List<Product> getWishlistItemsByUserId(Long userId);
    void addToWishlist(Long userId, Long productId);
    void removeFromWishlist(Long userId, Long productId);
    boolean isInWishlist(Long userId, Long productId);
}