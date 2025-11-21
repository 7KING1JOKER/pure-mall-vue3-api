package com.puremall.controller;

/**
 * 商品规格管理控制器
 * 处理商品规格的增删改查等操作
 */

import com.puremall.entity.ProductSpec;
import com.puremall.service.ProductSpecService;
import com.puremall.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product/spec")
@Tag(name = "商品规格管理", description = "商品规格查询接口")
public class ProductSpecController {

    @Autowired
    private ProductSpecService productSpecService;

    @GetMapping("/product/{productId}")
    @Operation(summary = "获取商品规格列表")
    public Response<List<ProductSpec>> getProductSpecs(@PathVariable Long productId) {
        List<ProductSpec> productSpecs = productSpecService.getProductSpecsByProductId(productId);
        return Response.success(productSpecs);
    }
}