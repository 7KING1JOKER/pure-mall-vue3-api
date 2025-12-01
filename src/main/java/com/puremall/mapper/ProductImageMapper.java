package com.puremall.mapper;

/**
 * 商品图片Mapper接口
 * 用于商品图片数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.ProductImage;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface ProductImageMapper extends BaseMapper<ProductImage> {
    // 根据商品ID查询图片列表
    @Select("SELECT * FROM product_images WHERE productId = #{productId} ORDER BY sortOrder ASC")
    List<ProductImage> findByProductId(Long productId);
    
    // 根据ID查询图片
    @Select("SELECT * FROM product_images WHERE id = #{id}")
    ProductImage findById(Long id);
    
    // 插入商品图片
    @Insert("INSERT INTO product_images(productId, imageUrl, sortOrder, createTime, updateTime) VALUES(#{productId}, #{imageUrl}, #{sortOrder}, NOW(), NOW()) ")
    int insert(ProductImage productImage);
    
    // 批量插入商品图片
    @Insert("<script>INSERT INTO product_images(productId, imageUrl, sortOrder, createTime, updateTime) VALUES <foreach item='item' collection='list' separator=','>(#{item.productId}, #{item.imageUrl}, #{item.sortOrder}, NOW(), NOW())</foreach></script>")
    int batchInsert(List<ProductImage> productImages);
    
    // 更新商品图片
    @Update("UPDATE product_images SET imageUrl = #{imageUrl}, sortOrder = #{sortOrder}, updateTime = NOW() WHERE id = #{id}")
    int update(ProductImage productImage);
    
    // 更新图片排序
    @Update("UPDATE product_images SET sortOrder = #{sortOrder}, updateTime = NOW() WHERE id = #{id}")
    int updateSort(Long id, Integer sortOrder);
    
    // 根据ID删除图片
    @Delete("DELETE FROM product_images WHERE id = #{id}")
    int deleteById(Long id);
    
    // 根据商品ID删除所有图片
    @Delete("DELETE FROM product_images WHERE productId = #{productId}")
    int deleteByProductId(Long productId);
    
    // 查询商品图片数量
    @Select("SELECT COUNT(*) FROM product_images WHERE productId = #{productId}")
    int countByProductId(Long productId);
}