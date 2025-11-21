package com.puremall.service.impl;

/**
 * 商品图片服务实现类
 * 实现商品图片相关的业务逻辑操作
 */

import com.puremall.entity.ProductImage;
import com.puremall.mapper.ProductImageMapper;
import com.puremall.service.ProductImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductImageServiceImpl extends ServiceImpl<ProductImageMapper, ProductImage> implements ProductImageService {

    @Autowired
    private ProductImageMapper productImageMapper;

    @Override
    public List<ProductImage> getProductImagesByProductId(Long productId) {
        return productImageMapper.findByProductId(productId);
    }
}