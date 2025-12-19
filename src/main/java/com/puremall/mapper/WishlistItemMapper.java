package com.puremall.mapper;

/**
 * 收藏夹商品项Mapper接口
 * 用于收藏夹商品项数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.WishlistItem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface WishlistItemMapper extends BaseMapper<WishlistItem> {
    // 根据用户ID查询收藏的商品列表
    @Select("SELECT * FROM wishlist_items WHERE userId = #{userId} ORDER BY createTime DESC")
    List<WishlistItem> findByUserId(Long userId);
    
    // 根据ID查询
    @Select("SELECT * FROM wishlist_items WHERE id = #{id}")
    WishlistItem findById(Long id);
    
    // 根据用户ID和商品ID查询
    @Select("SELECT * FROM wishlist_items WHERE userId = #{userId} AND productId = #{productId}")
    WishlistItem findByUserIdAndProductId(Long userId, Long productId);
    
    // 插入收藏记录
    @Insert("INSERT INTO wishlist_items(userId, productId, createTime) VALUES(#{userId}, #{productId},  NOW())")
    int insert(WishlistItem wishlistItem);
    
    // 更新收藏记录
    @Update("UPDATE wishlist_items SET userId = #{userId}, productId = #{productId}, WHERE id = #{id}")
    int update(WishlistItem wishlistItem);
    
    // 根据ID删除收藏记录
    @Delete("DELETE FROM wishlist_items WHERE id = #{id}")
    int deleteById(Long id);
    
    // 根据用户ID和商品ID删除收藏记录
    @Delete("DELETE FROM wishlist_items WHERE userId = #{userId} AND productId = #{productId}")
    int deleteByUserIdAndProductId(Long userId, Long productId);
    
    // 根据用户ID删除所有收藏记录
    @Delete("DELETE FROM wishlist_items WHERE userId = #{userId}")
    int deleteByUserId(Long userId);
    
    // 根据商品ID删除所有收藏记录
    @Delete("DELETE FROM wishlist_items WHERE productId = #{productId}")
    int deleteByProductId(Long productId);
    
    // 检查商品是否已收藏
    @Select("SELECT COUNT(*) FROM wishlist_items WHERE userId = #{userId} AND productId = #{productId}")
    int existsByUserIdAndProductId(Long userId, Long productId);
    
    // 根据用户ID统计收藏数量
    @Select("SELECT COUNT(*) FROM wishlist_items WHERE userId = #{userId}")
    int countByUserId(Long userId);
}