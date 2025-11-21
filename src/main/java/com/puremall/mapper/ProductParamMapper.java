package com.puremall.mapper;

/**
 * 商品参数Mapper接口
 * 用于商品参数数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.ProductParam;
import java.util.List;

public interface ProductParamMapper extends BaseMapper<ProductParam> {
    // 自定义查询方法
    List<ProductParam> findByProductId(Long productId);
}