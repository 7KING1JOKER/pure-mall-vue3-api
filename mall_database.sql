-- 纯商城系统(Pure Mall)数据库脚本
-- 版本: 1.0
-- 创建日期: 2023-07-20
-- 说明: 基于项目实体类和前端数据生成的完整数据库脚本

-- ============================ 使用说明 ============================
-- 1. 直接在MySQL客户端执行此脚本
-- 2. 确保MySQL版本支持utf8mb4字符集
-- 3. 脚本将自动创建数据库、所有表结构以及插入基础数据
-- 4. 默认管理员账号: admin / admin123
-- ================================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS pure_mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pure_mall;

-- 设置外键检查临时关闭，方便导入数据
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 用户表 (users)
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    last_login DATETIME COMMENT '最后登录时间',
    status TINYINT DEFAULT 1 COMMENT '状态（1:正常，0:禁用）',
    INDEX idx_username (username),
    INDEX idx_phone (phone),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 商品表 (products)
DROP TABLE IF EXISTS products;
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    name VARCHAR(255) NOT NULL COMMENT '商品名称',
    brief VARCHAR(500) COMMENT '商品简介',
    price DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '销售价格',
    original_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '原价',
    sales INT NOT NULL DEFAULT 0 COMMENT '销量',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存',
    category_label VARCHAR(50) DEFAULT NULL COMMENT '商品分类标签',
    detail LONGTEXT COMMENT '商品详情',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态（1:上架，0:下架）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name),
    INDEX idx_status (status),
    INDEX idx_category (category_label),
    INDEX idx_price (price),
    INDEX idx_sales (sales)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- 3. 商品图片表 (product_images)
DROP TABLE IF EXISTS product_images;
CREATE TABLE product_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '图片ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    image_url VARCHAR(500) NOT NULL COMMENT '图片URL',
    sort INT DEFAULT 0 COMMENT '排序',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品图片表';

-- 4. 商品规格表 (product_specs)
DROP TABLE IF EXISTS product_specs;
CREATE TABLE product_specs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '规格ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    name VARCHAR(255) NOT NULL COMMENT '规格名称',
    price DECIMAL(10, 2) NOT NULL COMMENT '规格价格',
    stock INT DEFAULT 0 COMMENT '库存',
    sales_amount INT DEFAULT 0 COMMENT '销量',
    color VARCHAR(50) COMMENT '颜色',
    size VARCHAR(50) COMMENT '尺码',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id),
    INDEX idx_stock (stock)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品规格表';

-- 5. 商品评价表 (product_reviews)
DROP TABLE IF EXISTS product_reviews;
CREATE TABLE product_reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评价ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    rating TINYINT NOT NULL COMMENT '评分（1-5星）',
    content TEXT COMMENT '评价内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id),
    INDEX idx_user_id (user_id),
    INDEX idx_rating (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品评价表';

-- 6. 地址表 (addresses)
DROP TABLE IF EXISTS addresses;
CREATE TABLE addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '地址ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    province VARCHAR(50) NOT NULL COMMENT '省份',
    city VARCHAR(50) NOT NULL COMMENT '城市',
    district VARCHAR(50) NOT NULL COMMENT '区县',
    street VARCHAR(255) COMMENT '街道',
    postcode VARCHAR(10) COMMENT '邮政编码',
    detail VARCHAR(255) NOT NULL COMMENT '详细地址',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否默认地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_is_default (is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收货地址表';

-- 7. 收藏夹表 (wishlists)
DROP TABLE IF EXISTS wishlists;
CREATE TABLE wishlists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏夹ID',
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏夹表';

-- 8. 收藏夹商品关联表 (wishlist_products)
DROP TABLE IF EXISTS wishlist_products;
CREATE TABLE wishlist_products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID',
    wishlist_id BIGINT NOT NULL COMMENT '收藏夹ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    add_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    UNIQUE KEY uk_wishlist_product (wishlist_id, product_id),
    FOREIGN KEY (wishlist_id) REFERENCES wishlists(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_wishlist_id (wishlist_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏夹商品关联表';

-- 9. 购物车表 (carts)
DROP TABLE IF EXISTS carts;
CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车ID',
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

-- 10. 购物车项表 (cart_items)
DROP TABLE IF EXISTS cart_items;
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车项ID',
    cart_id BIGINT NOT NULL COMMENT '购物车ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    spec_id BIGINT COMMENT '规格ID',
    name VARCHAR(255) NOT NULL COMMENT '商品名称',
    image_url VARCHAR(500) COMMENT '商品图片URL',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    selected TINYINT DEFAULT 1 COMMENT '是否选中（1:选中，0:未选中）',
    price DECIMAL(10, 2) NOT NULL COMMENT '单价',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_cart_product_spec (cart_id, product_id, spec_id),
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (spec_id) REFERENCES product_specs(id) ON DELETE SET NULL,
    INDEX idx_cart_id (cart_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车商品项表';

-- 11. 订单表 (orders)
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_number VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_time DATETIME COMMENT '下单时间',
    payment_time DATETIME COMMENT '支付时间',
    delivery_time DATETIME COMMENT '发货时间',
    receive_time DATETIME COMMENT '收货时间',
    order_amount DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    payment_method VARCHAR(20) COMMENT '支付方式',
    status VARCHAR(20) NOT NULL COMMENT '订单状态',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    receiver_address VARCHAR(500) NOT NULL COMMENT '收货地址',
    remark VARCHAR(500) COMMENT '订单备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_order_number (order_number),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_order_time (order_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 12. 订单项表 (order_items)
DROP TABLE IF EXISTS order_items;
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单项ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    spec_id BIGINT COMMENT '规格ID',
    name VARCHAR(255) NOT NULL COMMENT '商品名称',
    spec VARCHAR(255) COMMENT '规格描述',
    image_url VARCHAR(500) COMMENT '商品图片URL',
    price DECIMAL(10, 2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    selected BOOLEAN DEFAULT TRUE COMMENT '是否选中',
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (spec_id) REFERENCES product_specs(id) ON DELETE SET NULL,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单项表';

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 插入示例管理员用户
INSERT INTO users (username, password, email, phone, status) 
VALUES ('admin', 'admin123', 'admin@example.com', '13800138000', 1) 
ON DUPLICATE KEY UPDATE password = 'admin123';

-- 插入商品数据（基于前端data.txt）
-- 商品基础信息插入，匹配完整表结构
INSERT INTO products (id, name, brief, price, original_price, sales, stock, status, category_label) VALUES
(1001, '纯棉宽松短袖T恤', '舒适透气的纯棉短袖T恤，适合日常穿着', 99.0, 129.0, 1250, 5000, 1, 'T恤'),
(1002, '男士印花短袖T恤', '潮流印花设计，时尚百搭', 89.0, 119.0, 2100, 5000, 1, 'T恤'),
(1003, '女士修身短袖T恤', '修身版型，凸显身材曲线', 109.0, 139.0, 1850, 5000, 1, 'T恤'),
(1004, '情侣装短袖T恤', '甜蜜情侣款，展现恩爱', 119.0, 149.0, 980, 5000, 1, 'T恤'),
(1005, '商务休闲长袖衬衫', '正式场合必备，舒适透气', 199.0, 239.0, 890, 5000, 1, '衬衫'),
(1006, '男士牛津纺长袖衬衫', '经典牛津纺面料，质感出众', 219.0, 269.0, 1250, 5000, 1, '衬衫'),
(1007, '女士真丝长袖衬衫', '高级真丝面料，优雅奢华', 299.0, 369.0, 750, 5000, 1, '衬衫'),
(1008, '薄款防晒长袖衬衫', 'UPF50+防晒，轻薄透气', 159.0, 199.0, 1980, 5000, 1, '衬衫'),
(1009, '加绒连帽卫衣', '加厚保暖，时尚休闲', 259.0, 319.0, 1870, 5000, 1, '卫衣'),
(1010, '宽松圆领卫衣', 'oversize版型，潮流百搭', 199.0, 249.0, 2350, 5000, 1, '卫衣'),
(1011, '情侣款连帽卫衣', '情侣装设计，甜蜜同色系', 229.0, 289.0, 1450, 5000, 1, '卫衣'),
(1012, 'oversize卫衣', '超大版型，街头潮流', 249.0, 309.0, 1780, 5000, 1, '卫衣'),
(1013, '高领针织毛衣', '保暖高领，柔软亲肤', 299.0, 379.0, 670, 5000, 1, '毛衣'),
(1014, '圆领宽松毛衣', '宽松版型，舒适保暖', 279.0, 349.0, 1120, 5000, 1, '毛衣'),
(1015, 'V领针织开衫', '百搭V领，温柔气质', 329.0, 399.0, 890, 5000, 1, '毛衣'),
(1016, '麻花编织毛衣', '精致麻花编织，复古文艺', 359.0, 439.0, 580, 5000, 1, '毛衣'),
(1017, '修身牛仔夹克', '经典牛仔夹克，百搭不过时', 359.0, 429.0, 4320, 5000, 1, '夹克'),
(1018, '时尚西装外套', '时尚修身，商务休闲皆宜', 499.0, 599.0, 3250, 5000, 1, '外套'),
(1019, '休闲工装夹克', '多口袋设计，实用耐穿', 329.0, 399.0, 2180, 5000, 1, '夹克'),
(1020, '轻薄羽绒外套', '轻盈保暖，冬季必备', 599.0, 719.0, 1590, 5000, 1, '外套'),
(1021, '经典直筒牛仔裤', '经典直筒版型，修饰腿型', 249.0, 319.0, 1560, 5000, 1, '牛仔裤'),
(1022, '修身小脚牛仔裤', '修身小脚，时尚显瘦', 269.0, 339.0, 2350, 5000, 1, '牛仔裤'),
(1023, '高腰阔腿牛仔裤', '高腰显瘦，阔腿遮肉', 289.0, 369.0, 1890, 5000, 1, '牛仔裤'),
(1024, '破洞牛仔裤', '潮流破洞设计，个性十足', 299.0, 379.0, 1650, 5000, 1, '牛仔裤'),
(1025, '休闲束脚运动裤', '舒适宽松，运动休闲', 179.0, 219.0, 4320, 5000, 1, '休闲裤'),
(1026, '潮流工装短裤', '多口袋设计，夏日必备', 159.0, 199.0, 780, 5000, 1, '休闲裤'),
(1027, '男士商务休闲裤', '商务场合必备，舒适得体', 219.0, 279.0, 1980, 5000, 1, '休闲裤'),
(1028, '女士阔腿休闲裤', '宽松舒适，百搭时尚', 229.0, 289.0, 1750, 5000, 1, '休闲裤'),
(1029, '优雅碎花连衣裙', '碎花设计，温柔优雅', 329.0, 409.0, 2980, 5000, 1, '裙子'),
(1030, '高腰A字半身裙', '高腰显瘦，A字版型遮肉', 199.0, 249.0, 2150, 5000, 1, '裙子'),
(1031, '针织包臀裙', '针织面料，勾勒曲线', 259.0, 319.0, 1320, 5000, 1, '裙子'),
(1032, '波西米亚长裙', '波西米亚风格，异域风情', 359.0, 449.0, 890, 5000, 1, '裙子'),
(1033, '透气网面运动鞋', '透气网面，舒适轻便', 399.0, 499.0, 2150, 5000, 1, '鞋子'),
(1034, '经典小白鞋', '百搭小白鞋，四季皆宜', 359.0, 449.0, 3250, 5000, 1, '鞋子'),
(1035, '马丁靴', '经典马丁靴，酷感十足', 459.0, 569.0, 1850, 5000, 1, '鞋子'),
(1036, '夏季凉鞋', '轻盈透气，夏日必备', 259.0, 319.0, 2750, 5000, 1, '鞋子'),
(1037, '时尚棒球帽', '潮流棒球帽，遮阳必备', 89.0, 109.0, 980, 5000, 1, '配饰'),
(1038, '羊毛针织围巾', '柔软羊毛，温暖舒适', 159.0, 199.0, 1320, 5000, 1, '配饰'),
(1039, '潮流墨镜', '时尚墨镜，防晒必备', 299.0, 379.0, 1750, 5000, 1, '配饰'),
(1040, '时尚手表', '石英机芯，精准计时', 599.0, 749.0, 890, 5000, 1, '配饰'),
(1041, '棉质内裤', '纯棉面料，舒适透气', 129.0, 159.0, 3520, 5000, 1, '内衣'),
(1042, '莫代尔睡衣套装', '莫代尔面料，柔软亲肤', 199.0, 249.0, 2450, 5000, 1, '内衣'),
(1043, '保暖内衣套装', '加厚保暖，冬季必备', 259.0, 329.0, 1890, 5000, 1, '内衣'),
(1044, '纯棉袜子', '纯棉面料，吸汗透气', 69.0, 89.0, 4780, 5000, 1, '内衣'),
(1045, '双肩背包', '大容量设计，实用百搭', 299.0, 379.0, 2150, 5000, 1, '箱包'),
(1046, '时尚手提包', '时尚设计，容量适中', 399.0, 499.0, 1780, 5000, 1, '箱包'),
(1047, '斜挎包', '轻便实用，日常必备', 259.0, 329.0, 2350, 5000, 1, '箱包'),
(1048, '旅行拉杆箱', '大容量，坚固耐用', 499.0, 629.0, 1580, 5000, 1, '箱包');

-- 商品图片数据插入
INSERT INTO product_images (product_id, image_url, sort) VALUES
-- 为每个商品添加至少一张图片，使用data.txt中的实际图片URL，并确保product_id与商品表一致
(1001, 'https://images.unsplash.com/photo-1581655353564-df123a1eb820?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1),
(1002, 'https://images.unsplash.com/photo-1688990982651-a5d751773eff?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjN8fHNob3J0JTIwc2xlZXZlJTIwdCUyMHNoaXJ0JTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1003, 'https://images.unsplash.com/photo-1688404970273-4d83251d3686?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mjh8fFdvbWVuJ3MlMjBULXNoaXJ0JTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&q=60&w=600', 1),
(1004, 'https://images.unsplash.com/photo-1739809006763-49663591bdcd?q=80&w=1049&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1),
(1005, 'https://images.unsplash.com/photo-1745270029066-8c1dfb37c03c?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fEJ1c2luZXNzJTIwY2FzdWFsJTIwbG9uZyUyMHNsZWV2ZSUyMHNoaXJ0JTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1006, 'https://images.unsplash.com/photo-1614495039153-e9cd13240469?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1),
(1007, 'https://images.unsplash.com/photo-1590588503704-9aaf7d2946d3?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8TGFkaWVzJyUyMFNpbGslMjBMb25nJTIwU2xlZXZlJTIwU2hpcnQlMjBibGFjayUyMGFuZCUyMHdoaXRlfGVufDB8fDB8fHww', 1),
(1008, 'https://images.unsplash.com/photo-1744963129109-3f3fdf8d0b8d?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8VGhpbiUyMGxvbmclMjBzbGVldmUlMjBzaGlydCUyMGJsYWNrJTIwYW5kJTIwd2hpdGV8ZW58MHx8MHx8fDA%3D', 1),
(1009, 'https://images.unsplash.com/photo-1622866654030-fb0958200023?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8N3x8aG9vZGllJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1010, 'https://images.unsplash.com/photo-1716004109499-95f01ec73e30?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjJ8fExvb3NlJTIwcm91bmQlMjBuZWNrJTIwc3dlYXRzaGlydCUyMGJsYWNrJTIwYW5kJTIwd2hpdGV8ZW58MHx8MHx8fDA%3D', 1),
(1011, 'https://images.unsplash.com/photo-1576018357765-f04766b34b6b?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fENvdXBsZSdzJTIwaG9vZGVkJTIwc3dlYXRzaGlydCUyMGJsYWNrJTIwYW5kJTIwd2hpdGV8ZW58MHx8MHx8fDA%3D', 1),
(1012, 'https://images.unsplash.com/photo-1580159851546-833dd8f26318?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8b3ZlcnNpemVkJTIwaG9vZGllJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1013, 'https://images.unsplash.com/photo-1647736878001-e96794c5877a?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8SGlnaCUyMG5lY2slMjBrbml0dGVkJTIwc3dlYXRlciUyMGJsYWNrJTIwYW5kJTIwd2hpdGV8ZW58MHx8MHx8fDA%3D', 1),
(1014, 'https://images.unsplash.com/photo-1734003066406-33aa5fba6821?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8TG9vc2UlMjBmaXR0aW5nJTIwcm91bmQlMjBuZWNrJTIwc3dlYXRlciUyMGJsYWNrJTIwYW5kJTIwd2hpdGV8ZW58MHx8MHx8fDA%3D', 1),
(1015, 'https://images.unsplash.com/photo-1739047832043-287c97d7312a?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NTJ8fFYlMjBuZWNrJTIwa25pdHRlZCUyMGNhcmRpZ2FuJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1016, 'https://plus.unsplash.com/premium_photo-1671135590215-ded219822a44?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTd8fFR3aXN0ZWQlMjBrbml0JTIwc3dlYXRlciUyMGJsYWNrJTIwYW5kJTIwd2hpdGV8ZW58MHx8MHx8fDA%3D', 1),
(1017, 'https://images.unsplash.com/photo-1727516299214-c4d54704b045?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8N3x8U3VlZGUlMjBkZW5pbSUyMGphY2tldCUyMGJsYWNrJTIwYW5kJTIwd2hpdGV8ZW58MHx8MHx8fDA%3D', 1),
(1018, 'https://plus.unsplash.com/premium_photo-1661328047229-aea8cde84a31?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTN8fGZhc2hpb25hYmxlJTIwc3VpdCUyMGphY2tldCUyMGJsYWNrJTIwYW5kJTIwd2hpdGV8ZW58MHx8MHx8fDA%3D', 1),
(1019, 'https://images.unsplash.com/photo-1632958978877-69406b688b11?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTh8fENhc3VhbCUyMHdvcmslMjBqYWNrZXQlMjBibGFjayUyMGFuZCUyMHdoaXRlfGVufDB8fDB8fHww', 1),
(1020, 'https://images.unsplash.com/photo-1699431763539-a83758780d6f?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8TGlnaHR3ZWlnaHQlMjBkb3duJTIwamFja2V0JTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1021, 'https://images.unsplash.com/photo-1601747465107-1beb1c562334?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8amVhbnMlMjBibGFjayUyMGFuZCUyMHdoaXRlfGVufDB8fDB8fHww', 1),
(1022, 'https://images.unsplash.com/photo-1626295077144-61f9eb15d25e?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fGplYW5zJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1023, 'https://images.unsplash.com/photo-1633833293795-6a0da546e91f?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjB8fGplYW5zJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1024, 'https://images.unsplash.com/photo-1647309330899-3195579594b8?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTh8fHJpcHBlZCUyMGplYW5zJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1025, 'https://images.unsplash.com/photo-1715833002251-64a42ab16fdc?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8Q2FzdWFsJTIwY3VmZmVkJTIwc3dlYXRwYW50cyUyMGJsYWNrJTIwYW5kJTIwd2hpdGV8ZW58MHx8MHx8fDA%3D', 1),
(1026, 'https://images.unsplash.com/photo-1516271099866-de31ba93ee4b?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fFRyZW5keSUyMGNhcmdvJTIwc2hvcnRzJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1027, 'https://images.unsplash.com/photo-1590463043718-50200caa16d1?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fGNhc3VhbCUyMHBhbnRzJTIwc2hvcnRzJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1028, 'https://images.unsplash.com/photo-1671394199549-fa5715c1e40c?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTF8fFdvbWVuJ3MlMjB3aWRlJTIwbGVnJTIwY2FzdWFsJTIwcGFudHMlMjBibGFjayUyMGFuZCUyMHdoaXRlfGVufDB8fDB8fHww', 1),
(1029, 'https://images.unsplash.com/photo-1594357937085-f17503c4e748?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZmxvcmFsJTIwZHJlc3MlMjBibGFjayUyMGFuZCUyMHdoaXRlfGVufDB8fDB8fHww', 1),
(1030, 'https://images.unsplash.com/photo-1591079823942-a86a154ccb05?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8SGlnaCUyMHdhaXN0ZWQlMjBBJTIwbGluZSUyMHNraXJ0JTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1031, 'https://images.unsplash.com/photo-1622533082651-68c1d92bc15b?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTh8fGRyZXNzJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1032, 'https://images.unsplash.com/photo-1632227899642-743d963c8bfc?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MzB8fGRyZXNzJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1033, 'https://images.unsplash.com/photo-1578314921455-34dd4626b38d?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTJ8fHNob2VzJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1034, 'https://images.unsplash.com/photo-1556812191-381c7e7d96d6?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTl8fHNob2VzJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1035, 'https://images.unsplash.com/photo-1631482665514-567048726eb1?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjJ8fHNob2VzJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1036, 'https://images.unsplash.com/photo-1561304211-f88bafcc4e22?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MzV8fHNob2VzJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1037, 'https://images.unsplash.com/photo-1689501663816-4c5b7f6688f0?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjR8fEJhc2ViYWxsJTIwY2FwJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1038, 'https://images.unsplash.com/photo-1642888823903-dca882a78b69?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NDR8fFdvb2wlMjBrbml0dGVkJTIwc2NhcmYlMjBibGFjayUyMGFuZCUyMHdoaXRlfGVufDB8fDB8fHww', 1),
(1039, 'https://images.unsplash.com/photo-1730167056729-22f2b34ee32c?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTJ8fFRyZW5keSUyMHN1bmdsYXNzZXMlMjBibGFjayUyMGFuZCUyMHdoaXRlfGVufDB8fDB8fHww', 1),
(1040, 'https://images.unsplash.com/photo-1722445423163-f57f92ea9f78?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8d2F0Y2glMjBibGFjayUyMGFuZCUyMHdoaXRlfGVufDB8fDB8fHww', 1),
(1041, 'https://images.unsplash.com/photo-1640765937555-6f413ed1d936?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mzl8fENvdHRvbiUyMHVuZGVyd2VhciUyMGJsYWNrJTIwYW5kJTIwd2hpdGV8ZW58MHx8MHx8fDA%3D', 1),
(1042, 'https://images.unsplash.com/photo-1736697421338-c361795c7191?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGFqYW1hJTIwc2V0JTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1043, 'https://images.unsplash.com/photo-1731267776886-90f90af75eb1?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8N3x8dGhlcm1hbCUyMHVuZGVyd2VhciUyMHNldCUyMGJsYWNrJTIwYW5kJTIwd2hpdGV8ZW58MHx8MHx8fDA%3D', 1),
(1044, 'https://images.unsplash.com/photo-1640026199235-c24aa417b552?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fFB1cmUlMjBjb3R0b24lMjBzb2NrcyUyMGJsYWNrJTIwYW5kJTIwd2hpdGV8ZW58MHx8MHx8fDA%3D', 1),
(1045, 'https://images.unsplash.com/photo-1541267732407-8f72c182cf11?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8QmFja3BhY2slMjBibGFjayUyMGFuZCUyMHdoaXRlfGVufDB8fDB8fHww', 1),
(1046, 'https://images.unsplash.com/photo-1614179689702-355944cd0918?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjB8fGZhc2hpb24lMjBoYW5kYmFnJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1),
(1047, 'https://images.unsplash.com/photo-1592480071809-f42c1dfd4939?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8U2hvdWxkZXIlMjBiYWclMjBibGFjayUyMGFuZCUyMHdoaXRlfGVufDB8fDB8fHww', 1),
(1048, 'https://plus.unsplash.com/premium_photo-1677838847809-9f0ed2fa2a86?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTd8fFRyYXZlbCUyMHN1aXRjYXNlJTIwYmxhY2slMjBhbmQlMjB3aGl0ZXxlbnwwfHwwfHx8MA%3D%3D', 1);


-- 为部分商品添加规格信息
INSERT INTO product_specs (product_id, name, price, stock, color, size) VALUES
-- 商品1的规格 (所有商品的默认规格)
(1, '', 50, 100, '白色', 'S'),
(1, '', 60, 80, '白色', 'M'),
(1, '', 70, 90, '白色', 'L'),
(1, '', 80, 70, '白色', 'XL'),
(1, '', 50, 100, '黑色', 'S'),
(1, '', 60, 80, '黑色', 'M'),
(1, '', 70, 90, '黑色', 'L'),
(1, '', 80, 70, '黑色', 'XL'),
(1, '', 50, 100, '灰色', 'S'),
(1, '', 60, 80, '灰色', 'M'),
(1, '', 70, 90, '灰色', 'L'),
(1, '', 80, 70, '灰色', 'XL'),


-- ============================ 脚本执行完毕 ============================
-- 数据库初始化完成！您现在可以：
-- 1. 启动后端服务并连接到数据库
-- 2. 使用默认管理员账号登录系统
-- 3. 开始使用商城系统的各项功能
-- ====================================================================