package com.puremall.mapper;

/**
 * 收藏夹Mapper接口
 * 用于收藏夹数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.Wishlist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface WishlistMapper extends BaseMapper<Wishlist> {
    // 根据用户ID查询收藏夹
    @Select("SELECT * FROM wishlists WHERE userId = #{userId}")
    Wishlist findByUserId(Long userId);
    
    // 根据ID查询收藏夹
    @Select("SELECT * FROM wishlists WHERE id = #{id}")
    Wishlist findById(Long id);
    
    // 插入收藏夹
    @Insert("INSERT INTO wishlists(userId, createTime, updateTime) VALUES(#{userId}, NOW(), NOW())")
    int insert(Wishlist wishlist);
    
    // 更新收藏夹
    @Update("UPDATE wishlists SET userId = #{userId}, createTime = #{createTime}, updateTime = NOW() WHERE id = #{id}")
    int update(Wishlist wishlist);
    
    // 删除收藏夹
    @Delete("DELETE FROM wishlists WHERE id = #{id}")
    int deleteById(Long id);
    
    // 根据用户ID删除收藏夹
    @Delete("DELETE FROM wishlists WHERE userId = #{userId}")
    int deleteByUserId(Long userId);
    
    // 检查用户是否已有收藏夹
    @Select("SELECT COUNT(*) FROM wishlists WHERE userId = #{userId}")
    int existsByUserId(Long userId);
}