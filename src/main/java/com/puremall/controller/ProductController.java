package com.puremall.controller;

/**
 * 商品管理控制器
 * 处理商品的增删改查等操作
 */

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
    public Response<?> getProductPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "12") Integer pageSize,
            @RequestParam(required = false) String categoryLabel) {
        return Response.success(productService.getProductPage(pageNum, pageSize, categoryLabel));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "获取商品详情")
    public Response<Product> getProductById(@PathVariable Long productId) {
        return Response.success(productService.getProductById(productId));
    }

    @GetMapping("/category/{categoryLabel}")
    @Operation(summary = "根据分类获取商品列表")
    public Response<?> getProductsByCategory(
            @PathVariable String categoryLabel,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        if (pageNum != null && pageSize != null) {
            // 分页查询
            return Response.success(productService.getProductPage(pageNum, pageSize, categoryLabel));
        } else {
            // 不分页查询
            List<Product> products = productService.getProductsByCategory(categoryLabel);
            return Response.success(products);
        }
    }
    
    @GetMapping("/productList")
    @Operation(summary = "获取所有商品列表")
    public Response<List<Product>> getAllProducts() {
        return Response.success(productService.selectAllProducts());
    }

}