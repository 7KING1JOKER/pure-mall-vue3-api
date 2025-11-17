package com.puremall.service.impl;

import com.puremall.entity.ProductReview;
import com.puremall.mapper.ProductReviewMapper;
import com.puremall.service.ProductReviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductReviewServiceImpl extends ServiceImpl<ProductReviewMapper, ProductReview> implements ProductReviewService {

    @Autowired
    private ProductReviewMapper productReviewMapper;

    @Override
    public List<ProductReview> getProductReviewsByProductId(Long productId) {
        return productReviewMapper.findByProductId(productId);
    }
}