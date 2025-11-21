package com.puremall.controller;

/**
 * 商品参数管理控制器
 * 处理商品参数的增删改查等操作
 */

import com.puremall.entity.ProductParam;
import com.puremall.service.ProductParamService;
import com.puremall.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product/param")
@Tag(name = "商品参数管理", description = "商品参数查询接口")
public class ProductParamController {

    @Autowired
    private ProductParamService productParamService;

    @GetMapping("/product/{productId}")
    @Operation(summary = "获取商品参数列表")
    public Response<List<ProductParam>> getProductParams(@PathVariable Long productId) {
        List<ProductParam> productParams = productParamService.getProductParamsByProductId(productId);
        return Response.success(productParams);
    }
}