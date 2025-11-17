package com.puremall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.ProductReview;
import java.util.List;

public interface ProductReviewMapper extends BaseMapper<ProductReview> {
    // 自定义查询方法
    List<ProductReview> findByProductId(Long productId);
}