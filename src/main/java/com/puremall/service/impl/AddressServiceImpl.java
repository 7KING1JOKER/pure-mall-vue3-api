package com.puremall.service.impl;

/**
 * 地址服务实现类
 * 实现地址相关的业务逻辑操作
 */

import com.puremall.entity.Address;
import com.puremall.mapper.AddressMapper;
import com.puremall.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puremall.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> getUserAddresses(Long userId) {
        return addressMapper.findByUserId(userId);
    }

    @Override
    public Address getDefaultAddress(Long userId) {
        return addressMapper.findDefaultByUserId(userId);
    }

    @Override
    public Address getAddressById(Long userId, Long addressId) {
        Address address = addressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在或无权限操作");
        }
        return address;
    }

    @Override
    @Transactional
    public Address addAddress(Long userId, Address address) {
        // 设置用户ID
        address.setUserId(userId);
        // 如果是默认地址，先将其他地址设为非默认
        if (address.getIsDefault()) {
            addressMapper.updateDefaultByUserId(userId, 0);
        } else {
            // 如果是第一个地址，自动设为默认
            List<Address> userAddresses = addressMapper.findByUserId(userId);
            if (userAddresses == null || userAddresses.isEmpty()) {
                address.setIsDefault(true);
            }
        }
        // 保存地址
        addressMapper.insert(address);
        return address;
    }

    @Override
    @Transactional
    public Address updateAddress(Long userId, Long addressId, Address address) {
        // 验证地址是否属于当前用户
        Address existingAddress = addressMapper.selectById(addressId);
        if (existingAddress == null || !existingAddress.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在或无权限操作");
        }
        // 设置地址ID和用户ID
        address.setId(addressId);
        address.setUserId(userId);
        // 如果是默认地址，先将其他地址设为非默认
        if (address.getIsDefault()) {
            addressMapper.updateDefaultByUserId(userId, 0);
        }
        // 更新地址
        addressMapper.updateById(address);
        return address;
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        // 验证地址是否属于当前用户
        Address address = addressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在或无权限操作");
        }
        // 删除地址
        addressMapper.deleteById(addressId);
        
        // 如果删除的是默认地址，且用户还有其他地址，则将第一个地址设为默认
        if (address.getIsDefault()) {
            List<Address> remainingAddresses = addressMapper.findByUserId(userId);
            if (remainingAddresses != null && !remainingAddresses.isEmpty()) {
                Address newDefault = remainingAddresses.get(0);
                newDefault.setIsDefault(true);
                addressMapper.updateById(newDefault);
            }
        }
    }

    @Override
    @Transactional
    public Map<String, Object> setDefaultAddress(Long userId, Long addressId) {
        // 验证地址是否属于当前用户
        Address address = addressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在或无权限操作");
        }
        // 先将所有地址设为非默认
        addressMapper.updateDefaultByUserId(userId, 0);
        // 将指定地址设为默认
        address.setIsDefault(true);
        addressMapper.updateById(address);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "设置默认地址成功");
        return result;
    }
    
    @Override
    public int getUserAddressCount(Long userId) {
        return addressMapper.countByUserId(userId);
    }
}