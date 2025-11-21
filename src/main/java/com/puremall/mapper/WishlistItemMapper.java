package com.puremall.mapper;

/**
 * 收藏夹商品项Mapper接口
 * 用于收藏夹商品项数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.WishlistItem;
import java.util.List;

public interface WishlistItemMapper extends BaseMapper<WishlistItem> {
    // 自定义查询方法
    List<WishlistItem> findByWishlistId(Long wishlistId);
    WishlistItem findByWishlistIdAndProductId(Long wishlistId, Long productId);
}