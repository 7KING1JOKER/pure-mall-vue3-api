package com.puremall.controller;

/**
 * 地址管理控制器
 * 处理用户地址的增删改查等操作
 */

import com.puremall.entity.Address;
import com.puremall.service.UserService;
import com.puremall.service.AddressService;
import com.puremall.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/address")
@Tag(name = "地址管理", description = "用户地址管理接口")
public class AddressController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/userAddressList")
    @Operation(summary = "获取用户地址列表")
    public Response<List<Address>> getAddresses(String username) {
        Long userId = userService.getUserIdByUsername(username);
        List<Address> addresses = addressService.getUserAddresses(userId);
        return Response.success(addresses);
    }

    @GetMapping("/default")
    @Operation(summary = "获取用户默认地址")
    public Response<Address> getDefaultAddress(Long userId) {
        Address address = addressService.getDefaultAddress(userId);
        return Response.success(address);
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "根据ID获取地址详情")
    public Response<Address> getAddressById(Long userId, 
                                          @PathVariable Long addressId) {
        Address address = addressService.getAddressById(userId, addressId);
        return Response.success(address);
    }

    @PostMapping("/addAddress")
    @Operation(summary = "添加地址")
    public Response<Address> addAddress(String username, @RequestBody Address address) {
        Long userId = userService.getUserIdByUsername(username);
        address.setUserId(userId);
        Address newAddress = addressService.addAddress(userId, address);
        return Response.success(newAddress);
    }

    @PutMapping("/updateAddress/{addressId}")
    @Operation(summary = "更新地址")
    public Response<Address> updateAddress(String username, 
                                         @RequestBody Address address) {
        Long userId = userService.getUserIdByUsername(username);
        Address updatedAddress = addressService.updateAddress(userId, address);
        return Response.success(updatedAddress);
    }

    @DeleteMapping("/deleteAddress/{addressId}")
    @Operation(summary = "删除地址")
    public Response<Void> deleteAddress(String username, 
                                      @PathVariable Long addressId) {
        Long userId = userService.getUserIdByUsername(username);
        addressService.deleteAddress(userId, addressId);
        return Response.success(null);
    }

    @PutMapping("/{addressId}/default")
    @Operation(summary = "设置默认地址")
    public Response<Map<String, Object>> setDefaultAddress(String username,
                                                         @PathVariable Long addressId) {
        Long userId = userService.getUserIdByUsername(username);
        Map<String, Object> result = addressService.setDefaultAddress(userId, addressId);
        return Response.success(result);
    }
    
    @GetMapping("/count")
    @Operation(summary = "获取用户地址数量")
    public Response<Integer> getAddressCount(Long userId) {
        int count = addressService.getUserAddressCount(userId);
        return Response.success(count);
    }
}