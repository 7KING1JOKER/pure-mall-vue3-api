package com.puremall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.WishlistItem;
import java.util.List;

public interface WishlistItemMapper extends BaseMapper<WishlistItem> {
    // 自定义查询方法
    List<WishlistItem> findByWishlistId(Long wishlistId);
    WishlistItem findByWishlistIdAndProductId(Long wishlistId, Long productId);
}