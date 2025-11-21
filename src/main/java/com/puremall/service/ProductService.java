package com.puremall.service;

/**
 * 商品服务接口
 * 提供商品相关的业务逻辑操作
 */

import com.puremall.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

public interface ProductService extends IService<Product> {
    IPage<Product> getProductPage(Integer page, Integer size, Long categoryId, String keyword);
    Product getProductById(Long productId);
    List<Product> getProductsByCategory(Long categoryId);
}