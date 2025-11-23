package com.puremall.mapper;

/**
 * 商品评价Mapper接口
 * 用于商品评价数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.ProductReview;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface ProductReviewMapper extends BaseMapper<ProductReview> {
    // 根据商品ID查询评论列表
    @Select("SELECT * FROM product_reviews WHERE productId = #{productId} ORDER BY createTime DESC")
    List<ProductReview> findByProductId(Long productId);
    
    // 根据ID查询评论
    @Select("SELECT * FROM product_reviews WHERE id = #{id}")
    ProductReview findById(Long id);
    
    // 根据用户ID查询评论列表
    @Select("SELECT * FROM product_reviews WHERE userId = #{userId} ORDER BY createTime DESC")
    List<ProductReview> findByUserId(Long userId);
    
    // 根据商品ID和评分查询评价
    @Select("SELECT * FROM product_reviews WHERE product_id = #{productId} AND rating = #{rating} ORDER BY created_at DESC")
    List<ProductReview> findByProductIdAndRating(Long productId, Integer rating);
    
    // 插入评论
    @Insert("INSERT INTO product_reviews(productId, userId, rating, comment, createTime) VALUES(#{productId}, #{userId}, #{rating}, #{comment}, NOW())")
    int insert(ProductReview productReview);
    
    // 更新评论
    @Update("UPDATE product_reviews SET rating = #{rating}, comment = #{comment} WHERE id = #{id}")
    int update(ProductReview productReview);
    
    // 删除评论
    @Delete("DELETE FROM product_reviews WHERE id = #{id}")
    int deleteById(Long id);
    
    // 根据商品ID删除所有评价
    @Delete("DELETE FROM product_reviews WHERE product_id = #{productId}")
    int deleteByProductId(Long productId);
    
    // 根据用户ID删除所有评价
    @Delete("DELETE FROM product_reviews WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);
    
    // 查询商品评论数量
    @Select("SELECT COUNT(*) FROM product_reviews WHERE productId = #{productId}")
    int countByProductId(Long productId);
    
    // 查询商品平均评分
    @Select("SELECT AVG(rating) FROM product_reviews WHERE productId = #{productId}")
    Double getAverageRating(Long productId);
    
    // 根据评分统计评价数量
    @Select("SELECT rating, COUNT(*) as count FROM product_reviews WHERE product_id = #{productId} GROUP BY rating ORDER BY rating DESC")
    List<Object> countByRating(Long productId);
}