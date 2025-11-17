package com.puremall.service;

import com.puremall.entity.ProductSpec;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ProductSpecService extends IService<ProductSpec> {
    List<ProductSpec> getProductSpecsByProductId(Long productId);
}