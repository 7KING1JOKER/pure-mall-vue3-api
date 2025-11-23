package com.puremall.mapper;

/**
 * 地址Mapper接口
 * 用于地址数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.Address;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface AddressMapper extends BaseMapper<Address> {
    // 根据用户ID查询所有地址
    @Select("SELECT * FROM addresses WHERE user_id = #{userId} ORDER BY is_default DESC, updated_at DESC")
    List<Address> findByUserId(Long userId);
    
    // 查询用户默认地址
    @Select("SELECT * FROM addresses WHERE user_id = #{userId} AND is_default = 1")
    Address findDefaultByUserId(Long userId);
    
    // 更新用户所有地址的默认状态
    @Update("UPDATE addresses SET is_default = #{isDefault}, updated_at = NOW() WHERE user_id = #{userId}")
    void updateDefaultByUserId(Long userId, int isDefault);
    
    // 根据ID查询地址
    @Select("SELECT * FROM addresses WHERE id = #{id}")
    Address findById(Long id);
    
    // 插入地址
    @Insert("INSERT INTO addresses(user_id, name, phone, province, city, district, detail, is_default, created_at, updated_at) VALUES(#{userId}, #{name}, #{phone}, #{province}, #{city}, #{district}, #{detail}, #{isDefault}, NOW(), NOW())")
    int insert(Address address);
    
    // 更新地址
    @Update("UPDATE addresses SET name = #{name}, phone = #{phone}, province = #{province}, city = #{city}, district = #{district}, detail = #{detail}, is_default = #{isDefault}, updated_at = NOW() WHERE id = #{id} AND user_id = #{userId}")
    int update(Address address);
    
    // 设置默认地址
    @Update("UPDATE addresses SET is_default = 1, updated_at = NOW() WHERE id = #{id} AND user_id = #{userId}")
    int setDefault(Long id, Long userId);
    
    // 根据ID删除地址
    @Delete("DELETE FROM addresses WHERE id = #{id} AND user_id = #{userId}")
    int deleteById(Long id, Long userId);
    
    // 检查地址是否存在且属于该用户
    @Select("SELECT COUNT(*) FROM addresses WHERE id = #{id} AND user_id = #{userId}")
    int existsByIdAndUserId(Long id, Long userId);
}