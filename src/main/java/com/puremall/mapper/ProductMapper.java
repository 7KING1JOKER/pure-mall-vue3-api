package com.puremall.mapper;

/**
 * 商品Mapper接口
 * 用于商品数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puremall.entity.Product;

public interface ProductMapper extends BaseMapper<Product> {
    // 自定义查询方法
    IPage<Product> selectProductPage(Page<Product> page, Long categoryId, String keyword);
}