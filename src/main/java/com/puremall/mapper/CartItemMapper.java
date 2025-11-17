package com.puremall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.CartItem;
import java.util.List;

public interface CartItemMapper extends BaseMapper<CartItem> {
    // 自定义查询方法
    List<CartItem> findByCartId(Long cartId);
    CartItem findByCartIdAndProductIdAndSpecId(Long cartId, Long productId, Long specId);
}