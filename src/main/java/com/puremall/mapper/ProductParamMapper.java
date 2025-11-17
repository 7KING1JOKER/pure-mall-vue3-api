package com.puremall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.ProductParam;
import java.util.List;

public interface ProductParamMapper extends BaseMapper<ProductParam> {
    // 自定义查询方法
    List<ProductParam> findByProductId(Long productId);
}