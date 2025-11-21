package com.puremall.mapper;

/**
 * 购物车Mapper接口
 * 用于购物车数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.Cart;

public interface CartMapper extends BaseMapper<Cart> {
    // 自定义查询方法
    Cart findByUserId(Long userId);
}