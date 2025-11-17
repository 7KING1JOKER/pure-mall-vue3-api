package com.puremall.service;

import com.puremall.entity.Cart;
import com.puremall.entity.CartItem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface CartService extends IService<Cart> {
    Cart getCart(Long userId);
    List<CartItem> getCartItems(Long cartId);
    void addToCart(Long userId, CartItem cartItem);
    void updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity);
    void deleteCartItem(Long userId, Long cartItemId);
    void toggleCartItemSelected(Long userId, Long cartItemId);
    void toggleAllCartItemsSelected(Long userId, Boolean selected);
}