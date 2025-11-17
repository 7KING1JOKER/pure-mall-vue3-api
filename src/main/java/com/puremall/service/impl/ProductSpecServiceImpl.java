package com.puremall.service.impl;

import com.puremall.entity.ProductSpec;
import com.puremall.mapper.ProductSpecMapper;
import com.puremall.service.ProductSpecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductSpecServiceImpl extends ServiceImpl<ProductSpecMapper, ProductSpec> implements ProductSpecService {

    @Autowired
    private ProductSpecMapper productSpecMapper;

    @Override
    public List<ProductSpec> getProductSpecsByProductId(Long productId) {
        return productSpecMapper.findByProductId(productId);
    }
}