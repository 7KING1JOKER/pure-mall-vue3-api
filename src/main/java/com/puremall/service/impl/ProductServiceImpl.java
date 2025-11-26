package com.puremall.service.impl;

/**
 * 商品服务实现类
 * 实现商品相关的业务逻辑操作
 */

import com.puremall.entity.Product;
import com.puremall.entity.ProductImage;
import com.puremall.mapper.ProductMapper;
import com.puremall.mapper.ProductImageMapper;
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
    
    @Autowired
    private ProductImageMapper productImageMapper;
    

    @Override
    public IPage<Product> getProductPage(Integer page, Integer size, Long categoryId, String keyword) {
        Page<Product> productPage = new Page<>(page, size);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        
        // 商品状态过滤：只显示正常状态的商品
        queryWrapper.eq("status", 1);
        
        // 分类过滤
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }
        
        // 关键词搜索
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("name", keyword).or().like("brief", keyword);
        }
        
        // 默认排序
        queryWrapper.orderByDesc("create_time");
        
        IPage<Product> resultPage = productMapper.selectPage(productPage, queryWrapper);
        
        // 为每个商品加载第一张图片作为封面图
        for (Product product : resultPage.getRecords()) {
            List<ProductImage> images = productImageMapper.selectList(
                new QueryWrapper<ProductImage>()
                    .eq("product_id", product.getId())
                    .orderByAsc("sort")
                    .last("LIMIT 1")
            );
            if (!images.isEmpty()) {
                product.setImages(images);
            }
        }
        
        return resultPage;
    }

    @Override
    public Product getProductById(Long productId) {
        Product product = productMapper.selectById(productId);
        if (product != null) {
            // 加载商品图片
            List<ProductImage> images = productImageMapper.selectList(
                new QueryWrapper<ProductImage>()
                    .eq("product_id", productId)
                    .orderByAsc("sort")
            );
            product.setImages(images);
            
            // 加载商品规格（暂时注释掉，因为未使用）
            // List<ProductSpec> specs = productSpecMapper.selectList(
            //     new QueryWrapper<ProductSpec>()
            //         .eq("product_id", productId)
            // );
            // 如果Product实体类支持设置specs，可以添加：product.setSpecs(specs);
        }
        return product;
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        List<Product> products = productMapper.selectList(
            new QueryWrapper<Product>()
                .eq("category_id", categoryId)
                .eq("status", 1)
                .orderByDesc("create_time")
        );
        
        // 为每个商品加载第一张图片
        for (Product product : products) {
            List<ProductImage> images = productImageMapper.selectList(
                new QueryWrapper<ProductImage>()
                    .eq("product_id", product.getId())
                    .orderByAsc("sort")
                    .last("LIMIT 1")
            );
            if (!images.isEmpty()) {
                product.setImages(images);
            }
        }
        
        return products;
    }
    

}