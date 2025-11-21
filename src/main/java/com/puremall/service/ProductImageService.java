package com.puremall.service;

/**
 * 商品图片服务接口
 * 提供商品图片相关的业务逻辑操作
 */

import com.puremall.entity.ProductImage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ProductImageService extends IService<ProductImage> {
    List<ProductImage> getProductImagesByProductId(Long productId);
}