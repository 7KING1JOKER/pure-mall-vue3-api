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
    // 根据收藏夹ID查询收藏的商品列表
    @Select("SELECT * FROM wishlist_items WHERE wishlistId = #{wishlistId} ORDER BY createTime DESC")
    List<WishlistItem> findByWishlistId(Long wishlistId);
    
    // 根据收藏夹ID和商品ID查询
    @Select("SELECT * FROM wishlist_items WHERE wishlistId = #{wishlistId} AND productId = #{productId}")
    WishlistItem findByWishlistIdAndProductId(Long wishlistId, Long productId);
    
    // 根据ID查询
    @Select("SELECT * FROM wishlist_items WHERE id = #{id}")
    WishlistItem findById(Long id);
    
    // 根据用户ID查询收藏的商品（通过关联查询）
    @Select("SELECT wi.* FROM wishlist_items wi JOIN wishlists w ON wi.wishlistId = w.id WHERE w.userId = #{userId} ORDER BY wi.createTime DESC")
    List<WishlistItem> findByUserId(Long userId);
    
    // 插入收藏记录
    @Insert("INSERT INTO wishlist_items(wishlistId, productId, createTime, updateTime) VALUES(#{wishlistId}, #{productId}, NOW(), NOW())")
    int insert(WishlistItem wishlistItem);
    
    // 更新收藏记录
    @Update("UPDATE wishlist_items SET wishlistId = #{wishlistId}, productId = #{productId}, createTime = #{createTime}, updateTime = NOW() WHERE id = #{id}")
    int update(WishlistItem wishlistItem);
    
    // 根据ID删除收藏记录
    @Delete("DELETE FROM wishlist_items WHERE id = #{id}")
    int deleteById(Long id);
    
    // 根据收藏夹ID和商品ID删除收藏记录
    @Delete("DELETE FROM wishlist_items WHERE wishlistId = #{wishlistId} AND productId = #{productId}")
    int deleteByWishlistIdAndProductId(Long wishlistId, Long productId);
    
    // 根据收藏夹ID删除所有收藏记录
    @Delete("DELETE FROM wishlist_items WHERE wishlistId = #{wishlistId}")
    int deleteByWishlistId(Long wishlistId);
    
    // 根据商品ID删除所有收藏记录
    @Delete("DELETE FROM wishlist_items WHERE productId = #{productId}")
    int deleteByProductId(Long productId);
    
    // 检查商品是否已收藏
    @Select("SELECT COUNT(*) FROM wishlist_items WHERE wishlistId = #{wishlistId} AND productId = #{productId}")
    int existsByWishlistIdAndProductId(Long wishlistId, Long productId);
    
    // 根据收藏夹ID统计收藏数量
    @Select("SELECT COUNT(*) FROM wishlist_items WHERE wishlistId = #{wishlistId}")
    int countByWishlistId(Long wishlistId);
}