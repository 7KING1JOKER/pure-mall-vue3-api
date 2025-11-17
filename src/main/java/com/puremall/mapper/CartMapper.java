package com.puremall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.Cart;

public interface CartMapper extends BaseMapper<Cart> {
    // 自定义查询方法
    Cart findByUserId(Long userId);
}