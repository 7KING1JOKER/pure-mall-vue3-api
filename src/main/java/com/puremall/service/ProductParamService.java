package com.puremall.service;

import com.puremall.entity.ProductParam;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ProductParamService extends IService<ProductParam> {
    List<ProductParam> getProductParamsByProductId(Long productId);
}