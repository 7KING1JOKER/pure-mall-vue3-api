package com.puremall.service;

import com.puremall.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface AddressService extends IService<Address> {
    List<Address> getAddressesByUserId(Long userId);
    Address getDefaultAddressByUserId(Long userId);
    Address addAddress(Long userId, Address address);
    Address updateAddress(Long userId, Long addressId, Address address);
    void deleteAddress(Long userId, Long addressId);
    void setDefaultAddress(Long userId, Long addressId);
}