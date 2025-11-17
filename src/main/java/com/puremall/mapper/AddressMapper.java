package com.puremall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.Address;
import java.util.List;

public interface AddressMapper extends BaseMapper<Address> {
    // 自定义查询方法
    List<Address> findByUserId(Long userId);
    Address findDefaultByUserId(Long userId);
    // 更新用户默认地址状态
    void updateDefaultByUserId(Long userId, int isDefault);
}