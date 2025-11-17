package com.puremall.controller;

import com.puremall.entity.ProductImage;
import com.puremall.service.ProductImageService;
import com.puremall.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product/image")
@Tag(name = "商品图片管理", description = "商品图片上传、查询接口")
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @GetMapping("/product/{productId}")
    @Operation(summary = "获取商品图片列表")
    public Response<List<ProductImage>> getProductImages(@PathVariable Long productId) {
        List<ProductImage> productImages = productImageService.getProductImagesByProductId(productId);
        return Response.success(productImages);
    }
}