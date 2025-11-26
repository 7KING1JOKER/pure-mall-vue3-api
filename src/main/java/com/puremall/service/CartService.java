package com.puremall.service;

/**
 * 购物车服务接口
 * 提供购物车相关的业务逻辑操作
 */

import com.puremall.entity.Cart;
import com.puremall.entity.CartItem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

public interface CartService extends IService<Cart> {
    Cart getCart(Long userId);
    List<CartItem> getCartItems(Long cartId);
    void addToCart(Long userId, CartItem cartItem);
    void updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity);
    void deleteCartItem(Long userId, Long cartItemId);
    void toggleCartItemSelected(Long userId, Long cartItemId);
    void toggleAllCartItemsSelected(Long userId, Boolean selected);
    Map<String, Object> getCartStatistics(Long userId);
    List<CartItem> getSelectedCartItems(Long userId);
    void deleteSelectedCartItems(Long userId);
    void clearCart(Long userId);
    void updateCartItemsBatch(Long userId, List<Map<String, Object>> items);
}