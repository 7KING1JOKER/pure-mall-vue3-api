package com.puremall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.ProductImage;
import java.util.List;

public interface ProductImageMapper extends BaseMapper<ProductImage> {
    // 自定义查询方法
    List<ProductImage> findByProductId(Long productId);
}