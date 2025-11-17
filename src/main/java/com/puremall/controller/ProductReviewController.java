package com.puremall.controller;

import com.puremall.entity.ProductReview;
import com.puremall.service.ProductReviewService;
import com.puremall.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product/review")
@Tag(name = "商品评价管理", description = "商品评价查询接口")
public class ProductReviewController {

    @Autowired
    private ProductReviewService productReviewService;

    @GetMapping("/product/{productId}")
    @Operation(summary = "获取商品评价列表")
    public Response<List<ProductReview>> getProductReviews(@PathVariable Long productId) {
        List<ProductReview> productReviews = productReviewService.getProductReviewsByProductId(productId);
        return Response.success(productReviews);
    }
}