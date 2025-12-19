package com.puremall.service.impl;

/**
 * 购物车服务实现类
 * 实现购物车相关的业务逻辑操作
 */

import com.puremall.entity.CartItem;
import com.puremall.mapper.CartItemMapper;
import com.puremall.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puremall.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
public class CartServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartService {


    @Autowired
    private CartItemMapper cartItemMapper;

    @Override
    public List<CartItem> getCartItems(Long userId) {
        // 获取用户购物车项列表
        List<CartItem> cartItems = cartItemMapper.findByUserId(userId);
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        return cartItems;
    }

    @Override
    public void addCartItems(Long userId, CartItem cartItem) {
        // 检查购物车中是否已存在该商品规格
        CartItem existingItem = cartItemMapper.findByUserIdAndProductIdAndSpec(userId, cartItem.getProductId(), cartItem.getSpec());
        
        if (existingItem != null) {
            // 已存在，更新数量
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
            cartItemMapper.updateById(existingItem);
        } else {
            // 不存在，添加新记录
            cartItem.setSelected(true); // 默认选中
            cartItemMapper.insert(cartItem);
        }
    }

    @Override
    public void deleteCartItemById(Long Id) {
        // 验证购物车项是否存在
        CartItem existingItem = cartItemMapper.findById(Id);
        
        if (existingItem == null) {
            throw new BusinessException("购物车项不存在");
        }
        
        // 删除购物车项
        cartItemMapper.deleteById(Id);
    }

    @Override
    public void toggleCartItemSelected(Long userId, Long cartItemId) {
        // 验证购物车项是否存在
        CartItem cartItem = cartItemMapper.findById(cartItemId);
        if (cartItem == null) {
            throw new BusinessException("购物车项不存在");
        }
        
        // 验证该购物车项是否属于当前用户
        if (!cartItem.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该购物车项");
        }
        
        // 切换选中状态
        cartItem.setSelected(!cartItem.getSelected());
        cartItemMapper.updateById(cartItem);
    }

    @Override
    public void toggleAllCartItemsSelected(Long userId, Boolean selected) {
        // 更新用户所有购物车项的选中状态
        QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        List<CartItem> cartItems = cartItemMapper.selectList(wrapper);
        
        for (CartItem cartItem : cartItems) {
            cartItem.setSelected(selected);
            cartItemMapper.updateById(cartItem);
        }
    }
    
    @Override
    public Map<String, Object> getCartStatistics(Long userId) {
        List<CartItem> cartItems = getCartItems(userId);
        
        Map<String, Object> statistics = new HashMap<>();
        int totalCount = 0; // 总商品数量
        int selectedCount = 0; // 选中商品数量
        BigDecimal totalAmount = BigDecimal.ZERO; // 选中商品总金额
        boolean allSelected = true; // 是否全选
        
        for (CartItem item : cartItems) {
            totalCount += item.getQuantity();
            if (item.getSelected()) {
                selectedCount += item.getQuantity();
                totalAmount = totalAmount.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
            } else {
                allSelected = false;
            }
        }
        
        statistics.put("totalCount", totalCount);
        statistics.put("selectedCount", selectedCount);
        statistics.put("totalAmount", totalAmount);
        statistics.put("allSelected", allSelected && !cartItems.isEmpty());
        
        return statistics;
    }
    
    @Override
    public List<CartItem> getSelectedCartItems(Long userId) {
        return cartItemMapper.findSelectedByUserId(userId);
    }
    
    @Override
    public void deleteSelectedCartItems(Long userId) {
        cartItemMapper.deleteSelectedByUserId(userId);
    }
    
    @Override
    public void clearCartItems(Long userId) {
        cartItemMapper.deleteByUserId(userId);
    } 

    @Override
    public void updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        // 验证购物车项是否存在且属于当前用户
        CartItem cartItem = cartItemMapper.selectById(cartItemId);
        if (cartItem == null || !cartItem.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在或无权限");
        }
        cartItemMapper.updateQuantity(cartItemId, quantity);
    }
}