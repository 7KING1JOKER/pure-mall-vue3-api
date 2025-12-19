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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    
    // @Autowired
    // private ProductImageMapper productImageMapper;
    
    @Override
    public List<Product> selectAllProducts() {
        return productMapper.selectAllProducts();
    }

    @Override
    public IPage<Product> getProductPage(Integer page, Integer size, String categoryLabel) {
        Page<Product> productPage = new Page<>(page, size);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        
        // 商品状态过滤：只显示正常状态的商品
        queryWrapper.eq("status", 1);
        
        // 分类过滤
        if (categoryLabel != null) {
            queryWrapper.eq("categoryLabel", categoryLabel);
        }
        
        // 默认排序
        queryWrapper.orderByDesc("createTime");
        
        IPage<Product> resultPage = productMapper.selectPage(productPage, queryWrapper);
        
        return resultPage;
    }

    @Override
    public Product getProductById(Long productId) {
        Product product = productMapper.selectById(productId);
        // 不再设置images属性，直接通过productImageMapper查询
        return product;
    }

    @Override
    public List<Product> getProductsByCategory(String categoryLabel) {
        List<Product> products = productMapper.selectList(
            new QueryWrapper<Product>()
                .eq("categoryLabel", categoryLabel)
                .eq("status", 1)
                .orderByDesc("createTime")
        );
        
        return products;
    }

}