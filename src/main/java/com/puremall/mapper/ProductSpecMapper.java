package com.puremall.mapper;

/**
 * 商品规格Mapper接口
 * 用于商品规格数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.ProductSpec;
import java.util.List;

public interface ProductSpecMapper extends BaseMapper<ProductSpec> {
    // 自定义查询方法
    List<ProductSpec> findByProductId(Long productId);
}