package com.puremall.service;

/**
 * 地址服务接口
 * 提供地址相关的业务逻辑操作
 */

import com.puremall.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

public interface AddressService extends IService<Address> {
    List<Address> getUserAddresses(Long userId);
    Address getDefaultAddress(Long userId);
    Address getAddressById(Long userId, Long addressId);
    Address addAddress(Long userId, Address address);
    Address updateAddress(Long userId, Address address);
    void deleteAddress(Long userId, Long addressId);
    Map<String, Object> setDefaultAddress(Long userId, Long addressId);
    int getUserAddressCount(Long userId);
}