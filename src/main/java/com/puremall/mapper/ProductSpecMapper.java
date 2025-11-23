package com.puremall.mapper;

/**
 * 商品规格Mapper接口
 * 用于商品规格数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.ProductSpec;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductSpecMapper extends BaseMapper<ProductSpec> {
    // 根据商品ID查询规格列表
    @Select("SELECT * FROM product_specs WHERE productId = #{productId} ORDER BY id ASC")
    List<ProductSpec> findByProductId(Long productId);
    
    // 根据ID查询商品规格
    @Select("SELECT * FROM product_specs WHERE id = #{id}")
    ProductSpec findById(Long id);
    
    // 根据商品ID和规格信息查询规格
    @Select("SELECT * FROM product_specs WHERE productId = #{productId} AND color = #{color} AND size = #{size}")
    ProductSpec findByProductIdAndSpec(Long productId, String color, String size);
    
    // 插入商品规格
    @Insert("INSERT INTO product_specs(productId, name, price, stock, color, size) VALUES(#{productId}, #{name}, #{price}, #{stock}, #{color}, #{size})")
    int insert(ProductSpec productSpec);
    
    // 批量插入商品规格
    @Insert("<script>INSERT INTO product_specs(productId, name, price, stock, color, size) VALUES <foreach item='item' collection='list' separator=','>(#{item.productId}, #{item.name}, #{item.price}, #{item.stock}, #{item.color}, #{item.size})</foreach></script>")
    int batchInsert(List<ProductSpec> productSpecs);
    
    // 更新商品规格
    @Update("UPDATE product_specs SET name = #{name}, price = #{price}, stock = #{stock}, color = #{color}, size = #{size} WHERE id = #{id}")
    int update(ProductSpec productSpec);
    
    // 更新规格库存
    @Update("UPDATE product_specs SET stock = stock - #{quantity} WHERE id = #{id} AND stock >= #{quantity}")
    int updateStock(@Param("id") Long id, @Param("quantity") Integer quantity);
    
    // 删除商品规格
    @Delete("DELETE FROM product_specs WHERE id = #{id}")
    int deleteById(Long id);
    
    // 根据商品ID删除所有规格
    @Delete("DELETE FROM product_specs WHERE productId = #{productId}")
    int deleteByProductId(Long productId);
    
    // 查询商品规格数量
    @Select("SELECT COUNT(*) FROM product_specs WHERE productId = #{productId}")
    int countByProductId(Long productId);
    
    // 根据颜色分组查询规格
    @Select("SELECT DISTINCT color FROM product_specs WHERE productId = #{productId}")
    List<String> findColorsByProductId(Long productId);
    
    // 根据颜色查询尺寸
    @Select("SELECT DISTINCT size FROM product_specs WHERE productId = #{productId} AND color = #{color}")
    List<String> findSizesByProductIdAndColor(Long productId, String color);
}