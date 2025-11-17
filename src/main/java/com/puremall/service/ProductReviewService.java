package com.puremall.service;

import com.puremall.entity.ProductReview;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ProductReviewService extends IService<ProductReview> {
    List<ProductReview> getProductReviewsByProductId(Long productId);
}