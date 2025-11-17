package com.puremall.service.impl;

import com.puremall.entity.Cart;
import com.puremall.entity.CartItem;
import com.puremall.entity.ProductSpec;
import com.puremall.mapper.CartMapper;
import com.puremall.mapper.CartItemMapper;
import com.puremall.mapper.ProductSpecMapper;
import com.puremall.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puremall.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private ProductSpecMapper productSpecMapper;

    @Override
    public Cart getCart(Long userId) {
        // 获取用户购物车，不存在则创建
        Cart cart = cartMapper.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cartMapper.insert(cart);
        }
        return cart;
    }

    @Override
    public List<CartItem> getCartItems(Long cartId) {
        return cartItemMapper.findByCartId(cartId);
    }

    @Override
    public void addToCart(Long userId, CartItem cartItem) {
        // 获取用户购物车
        Cart cart = getCart(userId);
        
        // 验证商品规格是否存在
        ProductSpec spec = productSpecMapper.selectById(cartItem.getSpecId());
        if (spec == null) {
            throw new BusinessException("商品规格不存在");
        }
        
        // 检查库存是否足够
        if (cartItem.getQuantity() > spec.getStock()) {
            throw new BusinessException("库存不足");
        }
        
        // 检查购物车中是否已存在该商品规格
        QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
        wrapper.eq("cart_id", cart.getId())
               .eq("product_id", cartItem.getProductId())
               .eq("spec_id", cartItem.getSpecId());
        CartItem existingItem = cartItemMapper.selectOne(wrapper);
        
        if (existingItem != null) {
            // 已存在，更新数量
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
            // 再次检查库存
            if (existingItem.getQuantity() > spec.getStock()) {
                throw new BusinessException("库存不足");
            }
            existingItem.setPrice(spec.getPrice());
            cartItemMapper.updateById(existingItem);
        } else {
            // 不存在，添加新记录
            cartItem.setCartId(cart.getId());
            cartItem.setSelected(1); // 默认选中
            cartItem.setPrice(spec.getPrice());
            cartItemMapper.insert(cartItem);
        }
    }

    @Override
    public void updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        // 验证购物车项是否存在
        CartItem cartItem = cartItemMapper.selectById(cartItemId);
        if (cartItem == null) {
            throw new BusinessException("购物车项不存在");
        }
        
        // 验证该购物车项是否属于当前用户
        Cart cart = cartMapper.selectById(cartItem.getCartId());
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该购物车项");
        }
        
        // 验证商品规格是否存在
        ProductSpec spec = productSpecMapper.selectById(cartItem.getSpecId());
        if (spec == null) {
            throw new BusinessException("商品规格不存在");
        }
        
        // 检查库存是否足够
        if (quantity > spec.getStock()) {
            throw new BusinessException("库存不足");
        }
        
        // 更新数量
        cartItem.setQuantity(quantity);
        cartItem.setPrice(spec.getPrice());
        cartItemMapper.updateById(cartItem);
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) {
        // 验证购物车项是否存在
        CartItem cartItem = cartItemMapper.selectById(cartItemId);
        if (cartItem == null) {
            throw new BusinessException("购物车项不存在");
        }
        
        // 验证该购物车项是否属于当前用户
        Cart cart = cartMapper.selectById(cartItem.getCartId());
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该购物车项");
        }
        
        // 删除购物车项
        cartItemMapper.deleteById(cartItemId);
    }

    @Override
    public void toggleCartItemSelected(Long userId, Long cartItemId) {
        // 验证购物车项是否存在
        CartItem cartItem = cartItemMapper.selectById(cartItemId);
        if (cartItem == null) {
            throw new BusinessException("购物车项不存在");
        }
        
        // 验证该购物车项是否属于当前用户
        Cart cart = cartMapper.selectById(cartItem.getCartId());
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该购物车项");
        }
        
        // 切换选中状态
        cartItem.setSelected(cartItem.getSelected() == 1 ? 0 : 1);
        cartItemMapper.updateById(cartItem);
    }

    @Override
    public void toggleAllCartItemsSelected(Long userId, Boolean selected) {
        // 获取用户购物车
        Cart cart = getCart(userId);
        
        // 更新所有购物车项的选中状态
        QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
        wrapper.eq("cart_id", cart.getId());
        List<CartItem> cartItems = cartItemMapper.selectList(wrapper);
        
        for (CartItem cartItem : cartItems) {
            cartItem.setSelected(selected ? 1 : 0);
            cartItemMapper.updateById(cartItem);
        }
    }
}