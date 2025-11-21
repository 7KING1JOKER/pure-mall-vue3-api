package com.puremall.service;

/**
 * 商品参数服务接口
 * 提供商品参数相关的业务逻辑操作
 */

import com.puremall.entity.ProductParam;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ProductParamService extends IService<ProductParam> {
    List<ProductParam> getProductParamsByProductId(Long productId);
}