package com.puremall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.puremall.entity.Product;
import com.puremall.service.ProductService;
import com.puremall.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@Tag(name = "商品管理", description = "商品查询接口")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/page")
    @Operation(summary = "分页获取商品列表")
    public Response<IPage<Product>> getProductPage(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                 @RequestParam(required = false) Long categoryId,
                                                 @RequestParam(required = false) String keyword) {
        IPage<Product> productPage = productService.getProductPage(pageNum, pageSize, categoryId, keyword);
        return Response.success(productPage);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "获取商品详情")
    public Response<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return Response.success(product);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类获取商品列表")
    public Response<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return Response.success(products);
    }
}