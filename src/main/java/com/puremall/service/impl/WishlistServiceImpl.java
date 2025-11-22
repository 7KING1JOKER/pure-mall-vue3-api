package com.puremall.service.impl;

/**
 * 收藏夹服务实现类
 * 实现收藏夹相关的业务逻辑操作
 */

import com.puremall.entity.Wishlist;
import com.puremall.entity.Product;
import com.puremall.mapper.WishlistMapper;
import com.puremall.service.WishlistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puremall.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class WishlistServiceImpl extends ServiceImpl<WishlistMapper, Wishlist> implements WishlistService {

    @Autowired
    private WishlistMapper wishlistMapper;

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
    public List<Product> getWishlistItems(Long wishlistId) {
        Wishlist wishlist = wishlistMapper.selectById(wishlistId);
        return wishlist.getWishListItems();
    }

    @Override
    public void addToWishlist(Long userId, Long productId) {
        // 获取用户收藏夹
        Wishlist wishlist = getWishlist(userId);
        
        // 检查是否已在收藏夹中
        if (wishlist.getWishListItems() != null && wishlist.getWishListItems().stream()
                .anyMatch(product -> product.getId().equals(productId))) {
            throw new BusinessException("该商品已在收藏夹中");
        }
        
        // 这里实际需要通过ProductService或ProductMapper获取Product对象
        // 然后添加到wishlist的products列表中
        // 由于没有ProductService的具体实现，这里简化处理
        Product product = new Product();
        product.setId(productId);
        if (wishlist.getWishListItems() == null) {
            wishlist.setWishListItems(new ArrayList<>());
        }
        wishlist.getWishListItems().add(product);
        
        // 更新收藏夹
        wishlistMapper.updateById(wishlist);
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {
        // 获取用户收藏夹
        Wishlist wishlist = getWishlist(userId);
        
        // 检查是否在收藏夹中并移除
        boolean removed = wishlist.getWishListItems() != null && 
                wishlist.getWishListItems().removeIf(product -> product.getId().equals(productId));
                
        if (!removed) {
            throw new BusinessException("该商品不在收藏夹中");
        }
        
        // 更新收藏夹
        wishlistMapper.updateById(wishlist);
    }

    @Override
    public boolean isInWishlist(Long userId, Long productId) {
        // 获取用户收藏夹
        Wishlist wishlist = getWishlist(userId);
        
        // 检查是否在收藏夹中
        return wishlist.getWishListItems() != null && 
                wishlist.getWishListItems().stream().anyMatch(product -> product.getId().equals(productId));
    }
}