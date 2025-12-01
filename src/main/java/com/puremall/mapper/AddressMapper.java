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
    @Select("SELECT * FROM addresses WHERE userId = #{userId} ORDER BY isDefault DESC, updateTime DESC")
    List<Address> findByUserId(Long userId);
    
    // 查询用户默认地址
    @Select("SELECT * FROM addresses WHERE userId = #{userId} AND isDefault = 1")
    Address findDefaultByUserId(Long userId);
    
    // 更新用户所有地址的默认状态
    @Update("UPDATE addresses SET isDefault = #{isDefault}, updateTime = NOW() WHERE userId = #{userId}")
    void updateDefaultByUserId(Long userId, int isDefault);
    
    // 根据ID查询地址
    @Select("SELECT * FROM addresses WHERE id = #{id}")
    Address findById(Long id);
    
    // 插入地址
    @Insert("INSERT INTO addresses(userId, name, phone, province, city, district, detail, isDefault, createTime, updateTime) VALUES(#{userId}, #{name}, #{phone}, #{province}, #{city}, #{district}, #{detail}, #{isDefault}, NOW(), NOW())")
    int insert(Address address);
    
    // 更新地址
    @Update("UPDATE addresses SET name = #{name}, phone = #{phone}, province = #{province}, city = #{city}, district = #{district}, detail = #{detail}, isDefault = #{isDefault}, updateTime = NOW() WHERE id = #{id} AND userId = #{userId}")
    int update(Address address);
    
    // 设置默认地址
    @Update("UPDATE addresses SET isDefault = 1, updateTime = NOW() WHERE id = #{id} AND userId = #{userId}")
    int setDefault(Long id, Long userId);
    
    // 根据ID删除地址
    @Delete("DELETE FROM addresses WHERE id = #{id} AND userId = #{userId}")
    int deleteById(Long id, Long userId);
    
    // 检查地址是否存在且属于该用户
    @Select("SELECT COUNT(*) FROM addresses WHERE id = #{id} AND userId = #{userId}")
    int existsByIdAndUserId(Long id, Long userId);
    
    // 统计用户地址数量
    @Select("SELECT COUNT(*) FROM addresses WHERE userId = #{userId}")
    int countByUserId(Long userId);
}