package com.puremall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.ProductSpec;
import java.util.List;

public interface ProductSpecMapper extends BaseMapper<ProductSpec> {
    // 自定义查询方法
    List<ProductSpec> findByProductId(Long productId);
}