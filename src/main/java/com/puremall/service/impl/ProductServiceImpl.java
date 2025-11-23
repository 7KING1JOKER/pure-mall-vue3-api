package com.puremall.service.impl;

/**
 * 商品服务实现类
 * 实现商品相关的业务逻辑操作
 */

import com.puremall.entity.Product;
import com.puremall.mapper.ProductMapper;
import com.puremall.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public IPage<Product> getProductPage(Integer page, Integer size, Long categoryId, String keyword) {
        Page<Product> productPage = new Page<>(page, size);
        // 将Long类型的categoryId转换为String类型再传递给mapper
        String categoryIdStr = categoryId != null ? categoryId.toString() : null;
        return productMapper.selectProductPage(productPage, categoryIdStr, keyword);
    }

    @Override
    public Product getProductById(Long productId) {
        return productMapper.selectById(productId);
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return productMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Product>()
                .eq("category_id", categoryId)
                .eq("status", 1)
                .orderByDesc("create_time"));
    }
}