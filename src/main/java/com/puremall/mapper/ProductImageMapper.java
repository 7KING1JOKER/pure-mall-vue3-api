package com.puremall.mapper;

/**
 * 商品图片Mapper接口
 * 用于商品图片数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.ProductImage;
import java.util.List;

public interface ProductImageMapper extends BaseMapper<ProductImage> {
    // 自定义查询方法
    List<ProductImage> findByProductId(Long productId);
}