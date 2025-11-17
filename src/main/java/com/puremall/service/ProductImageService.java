package com.puremall.service;

import com.puremall.entity.ProductImage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ProductImageService extends IService<ProductImage> {
    List<ProductImage> getProductImagesByProductId(Long productId);
}