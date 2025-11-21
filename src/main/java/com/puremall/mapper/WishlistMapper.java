package com.puremall.mapper;

/**
 * 收藏夹Mapper接口
 * 用于收藏夹数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.Wishlist;

public interface WishlistMapper extends BaseMapper<Wishlist> {
    // 自定义查询方法
    Wishlist findByUserId(Long userId);
}