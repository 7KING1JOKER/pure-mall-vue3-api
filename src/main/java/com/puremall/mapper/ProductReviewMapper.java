package com.puremall.mapper;

/**
 * 商品评价Mapper接口
 * 用于商品评价数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.ProductReview;
import java.util.List;

public interface ProductReviewMapper extends BaseMapper<ProductReview> {
    // 自定义查询方法
    List<ProductReview> findByProductId(Long productId);
}