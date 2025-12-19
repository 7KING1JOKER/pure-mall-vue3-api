package com.puremall.service.impl;

/**
 * 收藏夹商品项服务实现类
 * 提供收藏夹商品项相关的业务逻辑操作实现
 */

import com.puremall.entity.WishlistItem;
import com.puremall.entity.Product;
import com.puremall.service.WishlistItemService;
import com.puremall.mapper.WishlistItemMapper;
import com.puremall.mapper.ProductMapper;
import com.puremall.exception.BusinessException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class WishlistItemServiceImpl extends ServiceImpl<WishlistItemMapper, WishlistItem> implements WishlistItemService {

    @Autowired
    private WishlistItemMapper wishlistItemMapper;
    
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> getWishlistItemsByUserId(Long userId) {
        // 获取用户的所有收藏夹商品项
        List<WishlistItem> wishlistItems = wishlistItemMapper.findByUserId(userId);
        List<Product> products = new ArrayList<>();
        
        // 根据商品ID获取商品详细信息
        for (WishlistItem item : wishlistItems) {
            Product product = productMapper.selectById(item.getProductId());
            if (product != null) {
                products.add(product);
            }
        }
        
        return products;
    }

    @Override
    public void addToWishlist(Long userId, Long productId) {
        // 验证商品是否存在
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        
        // 检查商品是否已在收藏夹中
        if (isInWishlist(userId, productId)) {
            throw new BusinessException("该商品已在收藏夹中");
        }
        
        // 添加到收藏夹
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setUserId(userId);
        wishlistItem.setProductId(productId);
        wishlistItemMapper.insert(wishlistItem);
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {
        // 直接根据用户ID和商品ID删除
        int result = wishlistItemMapper.deleteByUserIdAndProductId(userId, productId);
        if (result == 0) {
            throw new BusinessException("该商品不在收藏夹中");
        }
    }

    @Override
    public boolean isInWishlist(Long userId, Long productId) {
        // 直接根据用户ID和商品ID查询
        WishlistItem item = wishlistItemMapper.findByUserIdAndProductId(userId, productId);
        return item != null;
    }
}