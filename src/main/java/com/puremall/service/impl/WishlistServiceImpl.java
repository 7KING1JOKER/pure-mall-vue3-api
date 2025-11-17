package com.puremall.service.impl;

import com.puremall.entity.Wishlist;
import com.puremall.entity.WishlistItem;
import com.puremall.mapper.WishlistMapper;
import com.puremall.mapper.WishlistItemMapper;
import com.puremall.service.WishlistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puremall.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishlistServiceImpl extends ServiceImpl<WishlistMapper, Wishlist> implements WishlistService {

    @Autowired
    private WishlistMapper wishlistMapper;

    @Autowired
    private WishlistItemMapper wishlistItemMapper;

    @Override
    public Wishlist getWishlist(Long userId) {
        // 获取用户收藏夹，不存在则创建
        Wishlist wishlist = wishlistMapper.findByUserId(userId);
        if (wishlist == null) {
            wishlist = new Wishlist();
            wishlist.setUserId(userId);
            wishlistMapper.insert(wishlist);
        }
        return wishlist;
    }

    @Override
    public List<WishlistItem> getWishlistItems(Long wishlistId) {
        return wishlistItemMapper.findByWishlistId(wishlistId);
    }

    @Override
    public void addToWishlist(Long userId, Long productId) {
        // 获取用户收藏夹
        Wishlist wishlist = getWishlist(userId);
        
        // 检查是否已在收藏夹中
        WishlistItem existingItem = wishlistItemMapper.findByWishlistIdAndProductId(wishlist.getId(), productId);
        if (existingItem != null) {
            throw new BusinessException("该商品已在收藏夹中");
        }
        
        // 添加到收藏夹
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setWishlistId(wishlist.getId());
        wishlistItem.setProductId(productId);
        wishlistItemMapper.insert(wishlistItem);
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {
        // 获取用户收藏夹
        Wishlist wishlist = getWishlist(userId);
        
        // 检查是否在收藏夹中
        WishlistItem existingItem = wishlistItemMapper.findByWishlistIdAndProductId(wishlist.getId(), productId);
        if (existingItem == null) {
            throw new BusinessException("该商品不在收藏夹中");
        }
        
        // 从收藏夹中移除
        wishlistItemMapper.deleteById(existingItem.getId());
    }

    @Override
    public boolean isInWishlist(Long userId, Long productId) {
        // 获取用户收藏夹
        Wishlist wishlist = getWishlist(userId);
        
        // 检查是否在收藏夹中
        WishlistItem existingItem = wishlistItemMapper.findByWishlistIdAndProductId(wishlist.getId(), productId);
        return existingItem != null;
    }
}