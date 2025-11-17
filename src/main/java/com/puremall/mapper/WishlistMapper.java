package com.puremall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.Wishlist;

public interface WishlistMapper extends BaseMapper<Wishlist> {
    // 自定义查询方法
    Wishlist findByUserId(Long userId);
}