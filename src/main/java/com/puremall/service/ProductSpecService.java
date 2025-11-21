package com.puremall.service;

/**
 * 商品规格服务接口
 * 提供商品规格相关的业务逻辑操作
 */

import com.puremall.entity.ProductSpec;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ProductSpecService extends IService<ProductSpec> {
    List<ProductSpec> getProductSpecsByProductId(Long productId);
}