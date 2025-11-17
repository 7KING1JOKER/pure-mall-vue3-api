package com.puremall.service.impl;

import com.puremall.entity.Address;
import com.puremall.mapper.AddressMapper;
import com.puremall.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puremall.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> getAddressesByUserId(Long userId) {
        return addressMapper.findByUserId(userId);
    }

    @Override
    public Address getDefaultAddressByUserId(Long userId) {
        return addressMapper.findDefaultByUserId(userId);
    }

    @Override
    public Address addAddress(Long userId, Address address) {
        address.setUserId(userId);
        // 如果是默认地址，先取消其他地址的默认状态
        if (address.getIsDefault() == 1) {
            addressMapper.updateDefaultByUserId(userId, 0);
        }
        addressMapper.insert(address);
        return address;
    }

    @Override
    public Address updateAddress(Long userId, Long addressId, Address address) {
        Address existingAddress = addressMapper.selectById(addressId);
        if (existingAddress == null) {
            throw new BusinessException("地址不存在");
        }
        if (!existingAddress.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该地址");
        }
        // 如果设置为默认地址，先取消其他地址的默认状态
        if (address.getIsDefault() == 1) {
            addressMapper.updateDefaultByUserId(userId, 0);
        }
        address.setId(addressId);
        address.setUserId(userId);
        addressMapper.updateById(address);
        return address;
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        Address existingAddress = addressMapper.selectById(addressId);
        if (existingAddress == null) {
            throw new BusinessException("地址不存在");
        }
        if (!existingAddress.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该地址");
        }
        addressMapper.deleteById(addressId);
    }

    @Override
    public void setDefaultAddress(Long userId, Long addressId) {
        Address existingAddress = addressMapper.selectById(addressId);
        if (existingAddress == null) {
            throw new BusinessException("地址不存在");
        }
        if (!existingAddress.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该地址");
        }
        // 取消其他地址的默认状态
        addressMapper.updateDefaultByUserId(userId, 0);
        // 设置当前地址为默认
        existingAddress.setIsDefault(1);
        addressMapper.updateById(existingAddress);
    }
}