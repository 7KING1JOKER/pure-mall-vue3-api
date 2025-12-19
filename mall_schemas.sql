-- 1. 用户表 (users)
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    lastLogin DATETIME COMMENT '最后登录时间',
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
    originalPrice DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '原价',
    sales INT NOT NULL DEFAULT 0 COMMENT '销量',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存',
    categoryLabel VARCHAR(50) DEFAULT NULL COMMENT '商品分类标签',
    detail LONGTEXT COMMENT '商品详情',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态（1:上架，0:下架）',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name),
    INDEX idx_status (status),
    INDEX idx_category (categoryLabel),
    INDEX idx_price (price),
    INDEX idx_sales (sales)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- 3. 商品图片表 (product_images)
DROP TABLE IF EXISTS product_images;
CREATE TABLE product_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '图片ID',
    productId BIGINT NOT NULL COMMENT '商品ID',
    imageUrl VARCHAR(500) NOT NULL COMMENT '图片URL',
    sortOrder INT DEFAULT 0 COMMENT '排序',
    FOREIGN KEY (productId) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_productId (productId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品图片表';

-- 4. 商品规格表 (product_specs)
DROP TABLE IF EXISTS product_specs;
CREATE TABLE product_specs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '规格ID',
    productId BIGINT NOT NULL COMMENT '商品ID',
    name VARCHAR(255) NOT NULL COMMENT '规格名称',
    price DECIMAL(10, 2) NOT NULL COMMENT '规格价格',
    stock INT DEFAULT 0 COMMENT '库存',
    salesAmount INT DEFAULT 0 COMMENT '销量',
    color VARCHAR(50) COMMENT '颜色',
    size VARCHAR(50) COMMENT '尺码',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (productId) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_productId (productId),
    INDEX idx_stock (stock)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品规格表';

-- 5. 商品评价表 (product_reviews)
DROP TABLE IF EXISTS product_reviews;
CREATE TABLE product_reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评价ID',
    productId BIGINT NOT NULL COMMENT '商品ID',
    userId BIGINT NOT NULL COMMENT '用户ID',
    rating TINYINT NOT NULL COMMENT '评分（1-5星）',
    content TEXT COMMENT '评价内容',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (productId) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_productId (productId),
    INDEX idx_userId (userId),
    INDEX idx_rating (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品评价表';

-- 6. 地址表 (addresses)
DROP TABLE IF EXISTS addresses;
CREATE TABLE addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '地址ID',
    userId BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    province VARCHAR(50) NOT NULL COMMENT '省份',
    city VARCHAR(50) NOT NULL COMMENT '城市',
    district VARCHAR(50) NOT NULL COMMENT '区县',
    street VARCHAR(255) COMMENT '街道',
    postcode VARCHAR(10) COMMENT '邮政编码',
    detail VARCHAR(255) NOT NULL COMMENT '详细地址',
    isDefault BOOLEAN DEFAULT FALSE COMMENT '是否默认地址',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_userId (userId),
    INDEX idx_isDefault (isDefault)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收货地址表';

-- 7. 收藏夹商品项表 (wishlist_items)
DROP TABLE IF EXISTS wishlist_items;
CREATE TABLE wishlist_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏夹商品项ID',
    userId BIGINT NOT NULL COMMENT '用户ID',
    productId BIGINT NOT NULL COMMENT '商品ID',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_product (userId, productId),
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (productId) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_userId (userId),
    INDEX idx_productId (productId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏夹商品项表';

-- 9. 购物车项表 (cart_items)
DROP TABLE IF EXISTS cart_items;
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车项ID',
    userId BIGINT NOT NULL COMMENT '用户ID',
    productId BIGINT NOT NULL COMMENT '商品ID',
    spec VARCHAR(255) COMMENT '规格描述',
    name VARCHAR(255) NOT NULL COMMENT '商品名称',
    imageUrl VARCHAR(500) COMMENT '商品图片URL',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    selected BOOLEAN DEFAULT TRUE COMMENT '是否选中（1:选中，0:未选中）',
    price DECIMAL(10, 2) NOT NULL COMMENT '单价',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_product_spec (userId, productId, spec),
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (productId) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_userId (userId),
    INDEX idx_productId (productId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车商品项表';

-- 11. 订单表 (orders)
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    orderNumber VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    userId BIGINT NOT NULL COMMENT '用户ID',
    orderTime DATETIME COMMENT '下单时间',
    paymentTime DATETIME COMMENT '支付时间',
    deliveryTime DATETIME COMMENT '发货时间',
    receiveTime DATETIME COMMENT '收货时间',
    orderAmount DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    paymentMethod VARCHAR(20) COMMENT '支付方式',
    status VARCHAR(20) NOT NULL COMMENT '订单状态',
    receiverName VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiverPhone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    receiverAddress VARCHAR(500) NOT NULL COMMENT '收货地址',
    remark VARCHAR(500) COMMENT '订单备注',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_orderNumber (orderNumber),
    INDEX idx_userId (userId),
    INDEX idx_status (status),
    INDEX idx_orderTime (orderTime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 12. 订单项表 (order_items)
DROP TABLE IF EXISTS order_items;
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单项ID',
    orderId BIGINT NOT NULL COMMENT '订单ID',
    productId BIGINT NOT NULL COMMENT '商品ID',
    specId BIGINT COMMENT '规格ID',
    name VARCHAR(255) NOT NULL COMMENT '商品名称',
    spec VARCHAR(255) COMMENT '规格描述',
    imageUrl VARCHAR(500) COMMENT '商品图片URL',
    price DECIMAL(10, 2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    selected BOOLEAN DEFAULT TRUE COMMENT '是否选中',
    FOREIGN KEY (orderId) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (productId) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (specId) REFERENCES product_specs(id) ON DELETE SET NULL,
    INDEX idx_orderId (orderId),
    INDEX idx_productId (productId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单项表';