package com.puremall.service.impl;

/**
 * 商品参数服务实现类
 * 实现商品参数相关的业务逻辑操作
 */

import com.puremall.entity.ProductParam;
import com.puremall.mapper.ProductParamMapper;
import com.puremall.service.ProductParamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductParamServiceImpl extends ServiceImpl<ProductParamMapper, ProductParam> implements ProductParamService {

    @Autowired
    private ProductParamMapper productParamMapper;

    @Override
    public List<ProductParam> getProductParamsByProductId(Long productId) {
        return productParamMapper.findByProductId(productId);
    }
}