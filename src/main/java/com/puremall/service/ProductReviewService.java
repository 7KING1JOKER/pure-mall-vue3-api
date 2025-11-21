package com.puremall.service;

/**
 * 商品评价服务接口
 * 提供商品评价相关的业务逻辑操作
 */

import com.puremall.entity.ProductReview;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ProductReviewService extends IService<ProductReview> {
    List<ProductReview> getProductReviewsByProductId(Long productId);
}