package com.puremall.controller;

import com.puremall.entity.Address;
import com.puremall.service.AddressService;
import com.puremall.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/address")
@Tag(name = "地址管理", description = "用户地址管理接口")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/list")
    @Operation(summary = "获取用户地址列表")
    public Response<List<Address>> getAddresses(@RequestParam Long userId) {
        List<Address> addresses = addressService.getAddressesByUserId(userId);
        return Response.success(addresses);
    }

    @GetMapping("/default")
    @Operation(summary = "获取用户默认地址")
    public Response<Address> getDefaultAddress(@RequestParam Long userId) {
        Address address = addressService.getDefaultAddressByUserId(userId);
        return Response.success(address);
    }

    @PostMapping("/")
    @Operation(summary = "添加地址")
    public Response<Address> addAddress(@RequestParam Long userId, @RequestBody Address address) {
        Address newAddress = addressService.addAddress(userId, address);
        return Response.success(newAddress);
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "更新地址")
    public Response<Address> updateAddress(@RequestParam Long userId, @PathVariable Long addressId, @RequestBody Address address) {
        Address updatedAddress = addressService.updateAddress(userId, addressId, address);
        return Response.success(updatedAddress);
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "删除地址")
    public Response<Void> deleteAddress(@RequestParam Long userId, @PathVariable Long addressId) {
        addressService.deleteAddress(userId, addressId);
        return Response.success(null);
    }

    @PutMapping("/{addressId}/default")
    @Operation(summary = "设置默认地址")
    public Response<Void> setDefaultAddress(@RequestParam Long userId, @PathVariable Long addressId) {
        addressService.setDefaultAddress(userId, addressId);
        return Response.success(null);
    }
}