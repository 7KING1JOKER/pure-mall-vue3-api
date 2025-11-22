package com.puremall.entity;

/**
 * 收藏夹实体类
 * 存储用户的收藏夹信息
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@TableName("wishlists")
public class Wishlist implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Date createTime;

    private List<Product> wishListItems;

    // getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Product> getWishListItems() {
        return wishListItems;
    }

    public void setWishListItems(List<Product> wishListItems) {
        this.wishListItems = wishListItems;
    }
}