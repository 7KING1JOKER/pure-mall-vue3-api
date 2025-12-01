package com.puremall.mapper;

/**
 * 商品Mapper接口
 * 用于商品数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puremall.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    // 分页查询商品
    @Select("SELECT * FROM products WHERE (categoryLabel = #{categoryId} OR #{categoryId} IS NULL) AND (name LIKE CONCAT('%', #{keyword}, '%') OR brief LIKE CONCAT('%', #{keyword}, '%') OR #{keyword} IS NULL) AND status = 1 ORDER BY updateTime DESC")
    IPage<Product> selectProductPage(Page<Product> page, String categoryId, String keyword);
    
    // 根据ID查询商品详情
    @Select("SELECT * FROM products WHERE id = #{id}")
    Product findById(Long id);
    
    // 根据ID列表查询商品
    @Select("<script>SELECT * FROM products WHERE id IN <foreach item='id' collection='ids' open='(' separator=',' close=')'>#{id}</foreach></script>")
    List<Product> findByIds(List<Long> ids);
    
    // 查询热门商品
    @Select("SELECT * FROM products WHERE status = 1 ORDER BY sales DESC LIMIT #{limit}")
    List<Product> findHotProducts(Integer limit);
    
    // 查询新品
    @Select("SELECT * FROM products WHERE status = 1 ORDER BY createTime DESC LIMIT #{limit}")
    List<Product> findNewProducts(Integer limit);
    
    // 根据分类标签查询商品
    @Select("SELECT * FROM products WHERE categoryLabel = #{categoryLabel} AND status = 1 ORDER BY updateTime DESC")
    List<Product> findByCategoryLabel(String categoryLabel);
    
    // 插入商品
    @Insert("INSERT INTO products(name, brief, description, price, originalPrice, sales, stock, status, categoryLabel, createTime, updateTime) VALUES(#{name}, #{brief}, #{description}, #{price}, #{originalPrice}, #{sales}, #{stock}, #{status}, #{categoryLabel}, NOW(), NOW())")
    int insert(Product product);
    
    // 更新商品信息
    @Update("UPDATE products SET name = #{name}, brief = #{brief}, description = #{description}, price = #{price}, originalPrice = #{originalPrice}, stock = #{stock}, status = #{status}, categoryLabel = #{categoryLabel}, updateTime = NOW() WHERE id = #{id}")
    int update(Product product);
    
    // 更新商品状态
    @Update("UPDATE products SET status = #{status}, updateTime = NOW() WHERE id = #{id}")
    int updateStatus(Long id, Integer status);
    
    // 更新商品库存
    @Update("UPDATE products SET stock = stock - #{quantity}, sales = sales + #{quantity}, updateTime = NOW() WHERE id = #{id} AND stock >= #{quantity}")
    int updateStock(Long id, Integer quantity);
    
    // 根据ID删除商品
    @Delete("DELETE FROM products WHERE id = #{id}")
    int deleteById(Long id);
    
    // 搜索商品
    @Select("SELECT * FROM products WHERE (name LIKE CONCAT('%', #{keyword}, '%') OR brief LIKE CONCAT('%', #{keyword}, '%')) AND status = 1 ORDER BY updateTime DESC")
    List<Product> searchProducts(String keyword);
}