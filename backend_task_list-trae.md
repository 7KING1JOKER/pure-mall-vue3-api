# 后端 + 数据库任务清单

## 1. 前端需求分析

### 1.1 核心功能模块

根据前端代码分析，系统包含以下核心功能模块：

1. **用户管理**：登录、注册、个人信息管理、地址管理
2. **商品管理**：商品列表、商品详情、分类筛选
3. **购物车管理**：添加商品、删除商品、修改数量、结算
4. **订单管理**：创建订单、支付、订单列表、订单详情
5. **收藏夹管理**：添加收藏、删除收藏、收藏列表

### 1.2 数据模型分析

#### 1.2.1 用户相关
```typescript
// 用户信息
interface User {
  id: number;
  username: string;
  password: string;
  email: string;
  phone: string;
  avatar: string;
  createTime: string;
  lastLogin: string;
}

// 地址信息
interface Address {
  id: number;
  name: string;
  phone: string;
  province: string;
  city: string;
  district: string;
  detail: string;
  isDefault: boolean;
}
```

#### 1.2.2 商品相关
```typescript
// 商品基本信息
interface Product {
  id: number;
  name: string;
  brief: string;
  price: number;
  originalPrice: number;
  sales: number;
  images: string[];
  specs: ProductSpec[];
  detail: string;
  params: ProductParam[];
  reviews: ProductReview[];
}

// 商品规格
interface ProductSpec {
  id: number;
  name: string;
  price: number;
  stock: number;
}

// 商品参数
interface ProductParam {
  name: string;
  value: string;
}

// 商品评论
interface ProductReview {
  id: number;
  user: string;
  avatar: string;
  rating: number;
  content: string;
  date: string;
}
```

#### 1.2.3 购物车相关
```typescript
// 购物车项
interface CartItem {
  id: number;
  productId: number;
  name: string;
  image: string;
  spec: string;
  price: number;
  quantity: number;
  selected: boolean;
}
```

#### 1.2.4 订单相关
```typescript
// 订单
interface Order {
  id: string;
  orderNumber: string;
  orderTime: string;
  paymentTime: string;
  orderAmount: number;
  paymentMethod: string;
  status: 'pending' | 'paid' | 'shipped' | 'delivered' | 'cancelled';
  deliveryInfo: {
    name: string;
    phone: string;
    address: string;
  };
  items: OrderItem[];
  remark?: string;
}

// 订单项
interface OrderItem {
  id: number;
  name: string;
  spec: string;
  price: number;
  quantity: number;
  image: string;
}
```

#### 1.2.5 收藏夹相关
```typescript
// 收藏夹项
interface WishlistItem {
  id: number;
  productId: number;
  name: string;
  image: string;
  price: number;
  selected: boolean;
}
```

### 1.3 API接口需求

根据前端`request.js`配置，后端API需要支持以下功能：

- 基于RESTful风格的API设计
- JWT令牌认证
- 统一的响应格式（包含code、message、data字段）
- 支持CORS跨域访问

## 2. 数据库设计

### 2.1 数据库表结构

#### 2.1.1 用户表 (users)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 用户ID |
| username | VARCHAR(50) | NOT NULL, UNIQUE | 用户名 |
| password | VARCHAR(100) | NOT NULL | 密码（加密存储） |
| email | VARCHAR(100) | NOT NULL, UNIQUE | 邮箱 |
| phone | VARCHAR(20) | NOT NULL, UNIQUE | 手机号 |
| avatar | VARCHAR(255) | DEFAULT NULL | 头像URL |
| create_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| last_login | DATETIME | DEFAULT NULL | 最后登录时间 |
| status | TINYINT | NOT NULL, DEFAULT 1 | 状态(1:正常, 0:禁用) |

#### 2.1.2 地址表 (addresses)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 地址ID |
| user_id | BIGINT | NOT NULL, FOREIGN KEY | 用户ID |
| name | VARCHAR(20) | NOT NULL | 收货人姓名 |
| phone | VARCHAR(20) | NOT NULL | 收货人电话 |
| province | VARCHAR(50) | NOT NULL | 省份 |
| city | VARCHAR(50) | NOT NULL | 城市 |
| district | VARCHAR(50) | NOT NULL | 区/县 |
| detail | VARCHAR(255) | NOT NULL | 详细地址 |
| is_default | TINYINT | NOT NULL, DEFAULT 0 | 是否默认地址(1:是, 0:否) |
| create_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

#### 2.1.3 商品分类表 (categories)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 分类ID |
| name | VARCHAR(50) | NOT NULL | 分类名称 |
| parent_id | BIGINT | DEFAULT NULL, FOREIGN KEY | 父分类ID |
| level | TINYINT | NOT NULL | 分类级别(1:一级, 2:二级) |
| sort | INT | NOT NULL, DEFAULT 0 | 排序 |
| create_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |

#### 2.1.4 商品表 (products)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 商品ID |
| name | VARCHAR(100) | NOT NULL | 商品名称 |
| brief | VARCHAR(255) | NOT NULL | 商品简介 |
| price | DECIMAL(10,2) | NOT NULL | 商品价格 |
| original_price | DECIMAL(10,2) | NOT NULL | 商品原价 |
| sales | INT | NOT NULL, DEFAULT 0 | 销量 |
| category_id | BIGINT | NOT NULL, FOREIGN KEY | 分类ID |
| detail | TEXT | NOT NULL | 商品详情 |
| status | TINYINT | NOT NULL, DEFAULT 1 | 状态(1:上架, 0:下架) |
| create_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

#### 2.1.5 商品规格表 (product_specs)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 规格ID |
| product_id | BIGINT | NOT NULL, FOREIGN KEY | 商品ID |
| name | VARCHAR(50) | NOT NULL | 规格名称(如:白色/黑色) |
| price | DECIMAL(10,2) | NOT NULL | 规格价格 |
| stock | INT | NOT NULL | 库存 |
| create_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |

#### 2.1.6 商品参数表 (product_params)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 参数ID |
| product_id | BIGINT | NOT NULL, FOREIGN KEY | 商品ID |
| param_name | VARCHAR(50) | NOT NULL | 参数名称(如:材质/版型) |
| param_value | VARCHAR(255) | NOT NULL | 参数值 |

#### 2.1.7 商品图片表 (product_images)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 图片ID |
| product_id | BIGINT | NOT NULL, FOREIGN KEY | 商品ID |
| image_url | VARCHAR(255) | NOT NULL | 图片URL |
| sort | INT | NOT NULL, DEFAULT 0 | 排序 |

#### 2.1.8 商品评论表 (product_reviews)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 评论ID |
| product_id | BIGINT | NOT NULL, FOREIGN KEY | 商品ID |
| user_id | BIGINT | NOT NULL, FOREIGN KEY | 用户ID |
| rating | TINYINT | NOT NULL | 评分(1-5) |
| content | TEXT | NOT NULL | 评论内容 |
| create_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |

#### 2.1.9 购物车表 (carts)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 购物车ID |
| user_id | BIGINT | NOT NULL, FOREIGN KEY, UNIQUE | 用户ID |
| create_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

#### 2.1.10 购物车项表 (cart_items)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 购物车项ID |
| cart_id | BIGINT | NOT NULL, FOREIGN KEY | 购物车ID |
| product_id | BIGINT | NOT NULL, FOREIGN KEY | 商品ID |
| spec_id | BIGINT | NOT NULL, FOREIGN KEY | 规格ID |
| quantity | INT | NOT NULL | 数量 |
| selected | TINYINT | NOT NULL, DEFAULT 1 | 是否选中(1:是, 0:否) |
| create_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

#### 2.1.11 收藏夹表 (wishlists)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 收藏夹ID |
| user_id | BIGINT | NOT NULL, FOREIGN KEY, UNIQUE | 用户ID |
| create_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |

#### 2.1.12 收藏夹项表 (wishlist_items)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 收藏夹项ID |
| wishlist_id | BIGINT | NOT NULL, FOREIGN KEY | 收藏夹ID |
| product_id | BIGINT | NOT NULL, FOREIGN KEY | 商品ID |
| create_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |

#### 2.1.13 订单表 (orders)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 订单ID |
| order_number | VARCHAR(32) | NOT NULL, UNIQUE | 订单号 |
| user_id | BIGINT | NOT NULL, FOREIGN KEY | 用户ID |
| order_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 下单时间 |
| payment_time | DATETIME | DEFAULT NULL | 支付时间 |
| delivery_time | DATETIME | DEFAULT NULL | 发货时间 |
| receive_time | DATETIME | DEFAULT NULL | 收货时间 |
| order_amount | DECIMAL(10,2) | NOT NULL | 订单金额 |
| payment_method | VARCHAR(20) | NOT NULL | 支付方式(alipay/wechat/creditcard) |
| status | VARCHAR(20) | NOT NULL | 订单状态(pending/paid/shipped/delivered/cancelled) |
| receiver_name | VARCHAR(20) | NOT NULL | 收货人姓名 |
| receiver_phone | VARCHAR(20) | NOT NULL | 收货人电话 |
| receiver_address | VARCHAR(255) | NOT NULL | 收货地址 |
| remark | VARCHAR(255) | DEFAULT NULL | 订单备注 |
| create_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

#### 2.1.14 订单项表 (order_items)
| 字段名 | 数据类型 | 约束 | 描述 |
| ------ | -------- | ---- | ---- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 订单项ID |
| order_id | BIGINT | NOT NULL, FOREIGN KEY | 订单ID |
| product_id | BIGINT | NOT NULL, FOREIGN KEY | 商品ID |
| spec_id | BIGINT | NOT NULL, FOREIGN KEY | 规格ID |
| product_name | VARCHAR(100) | NOT NULL | 商品名称 |
| spec_name | VARCHAR(50) | NOT NULL | 规格名称 |
| price | DECIMAL(10,2) | NOT NULL | 单价 |
| quantity | INT | NOT NULL | 数量 |
| image_url | VARCHAR(255) | NOT NULL | 商品图片URL |

### 2.2 数据库关系图

```
用户(users) 1 --- N 地址(addresses)
用户(users) 1 --- 1 购物车(carts)
用户(users) 1 --- 1 收藏夹(wishlists)
用户(users) 1 --- N 订单(orders)
用户(users) 1 --- N 商品评论(product_reviews)

分类(categories) 1 --- N 商品(products)

商品(products) 1 --- N 商品规格(product_specs)
商品(products) 1 --- N 商品参数(product_params)
商品(products) 1 --- N 商品图片(product_images)
商品(products) 1 --- N 商品评论(product_reviews)

购物车(carts) 1 --- N 购物车项(cart_items)
商品(products) 1 --- N 购物车项(cart_items)
商品规格(product_specs) 1 --- N 购物车项(cart_items)

收藏夹(wishlists) 1 --- N 收藏夹项(wishlist_items)
商品(products) 1 --- N 收藏夹项(wishlist_items)

订单(orders) 1 --- N 订单项(order_items)
商品(products) 1 --- N 订单项(order_items)
商品规格(product_specs) 1 --- N 订单项(order_items)
```

## 3. 后端项目结构

### 3.1 技术栈

- **框架**：Spring Boot 3.2.x
- **持久层**：MyBatis Plus
- **数据库**：MySQL 8.0
- **认证**：JWT (JSON Web Token)
- **API文档**：Swagger/OpenAPI 3.0
- **构建工具**：Maven

### 3.2 项目目录结构

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── puremall/
│   │           ├── PureMallApplication.java       # 应用入口
│   │           ├── config/                        # 配置类
│   │           │   ├── MyBatisConfig.java         # MyBatis配置
│   │           │   ├── SecurityConfig.java        # 安全配置
│   │           │   ├── JwtConfig.java             # JWT配置
│   │           │   └── CorsConfig.java            # CORS配置
│   │           ├── controller/                    # 控制器
│   │           │   ├── UserController.java        # 用户相关接口
│   │           │   ├── AddressController.java     # 地址相关接口
│   │           │   ├── ProductController.java     # 商品相关接口
│   │           │   ├── CartController.java        # 购物车相关接口
│   │           │   ├── WishlistController.java    # 收藏夹相关接口
│   │           │   └── OrderController.java       # 订单相关接口
│   │           ├── service/                       # 服务层
│   │           │   ├── UserService.java           # 用户服务
│   │           │   ├── AddressService.java        # 地址服务
│   │           │   ├── ProductService.java        # 商品服务
│   │           │   ├── CartService.java           # 购物车服务
│   │           │   ├── WishlistService.java       # 收藏夹服务
│   │           │   └── OrderService.java          # 订单服务
│   │           ├── mapper/                        # 数据访问层
│   │           │   ├── UserMapper.java            # 用户Mapper
│   │           │   ├── AddressMapper.java         # 地址Mapper
│   │           │   ├── ProductMapper.java         # 商品Mapper
│   │           │   ├── ProductSpecMapper.java     # 商品规格Mapper
│   │           │   ├── ProductParamMapper.java    # 商品参数Mapper
│   │           │   ├── ProductImageMapper.java    # 商品图片Mapper
│   │           │   ├── ProductReviewMapper.java   # 商品评论Mapper
│   │           │   ├── CartMapper.java            # 购物车Mapper
│   │           │   ├── CartItemMapper.java        # 购物车项Mapper
│   │           │   ├── WishlistMapper.java        # 收藏夹Mapper
│   │           │   ├── WishlistItemMapper.java    # 收藏夹项Mapper
│   │           │   ├── OrderMapper.java           # 订单Mapper
│   │           │   └── OrderItemMapper.java       # 订单项Mapper
│   │           ├── entity/                        # 实体类
│   │           │   ├── User.java                  # 用户实体
│   │           │   ├── Address.java               # 地址实体
│   │           │   ├── Product.java               # 商品实体
│   │           │   ├── ProductSpec.java           # 商品规格实体
│   │           │   ├── ProductParam.java          # 商品参数实体
│   │           │   ├── ProductImage.java          # 商品图片实体
│   │           │   ├── ProductReview.java         # 商品评论实体
│   │           │   ├── Cart.java                  # 购物车实体
│   │           │   ├── CartItem.java              # 购物车项实体
│   │           │   ├── Wishlist.java              # 收藏夹实体
│   │           │   ├── WishlistItem.java          # 收藏夹项实体
│   │           │   ├── Order.java                 # 订单实体
│   │           │   ├── OrderItem.java             # 订单项实体
│   │           │   └── Response.java              # 统一响应实体
│   │           ├── exception/                     # 异常处理
│   │           │   ├── GlobalExceptionHandler.java # 全局异常处理
│   │           │   └── BusinessException.java     # 业务异常
│   │           └── utils/                         # 工具类
│   │               ├── JwtUtils.java              # JWT工具
│   │               ├── PasswordUtils.java         # 密码工具
│   │               └── OrderNumberUtils.java      # 订单号生成工具
│   └── resources/
│       ├── application.yml                        # 应用配置
│       ├── application-dev.yml                    # 开发环境配置
│       ├── application-prod.yml                   # 生产环境配置
│       └── mapper/                                # MyBatis映射文件
│           ├── UserMapper.xml                     # 用户Mapper映射
│           ├── ProductMapper.xml                  # 商品Mapper映射
│           ├── ProductSpecMapper.xml              # 商品规格Mapper映射
│           ├── ProductParamMapper.xml             # 商品参数Mapper映射
│           ├── ProductImageMapper.xml             # 商品图片Mapper映射
│           ├── ProductReviewMapper.xml            # 商品评论Mapper映射
│           ├── CartMapper.xml                     # 购物车Mapper映射
│           ├── CartItemMapper.xml                 # 购物车项Mapper映射
│           ├── WishlistMapper.xml                 # 收藏夹Mapper映射
│           ├── WishlistItemMapper.xml             # 收藏夹项Mapper映射
│           ├── OrderMapper.xml                    # 订单Mapper映射
│           └── OrderItemMapper.xml                # 订单项Mapper映射
└── test/                                          # 测试目录
    └── java/
        └── com/
            └── puremall/
                └── service/
                    ├── UserServiceTest.java       # 用户服务测试
                    ├── ProductServiceTest.java    # 商品服务测试
                    └── OrderServiceTest.java      # 订单服务测试
```

## 4. 后端实现步骤

### 4.1 项目初始化

1. **创建Spring Boot项目**
   - 使用Spring Initializr创建项目
   - 选择依赖：Spring Web, Spring Boot DevTools, MySQL Driver, MyBatis Plus Generator
   - 项目名称：pure-mall-backend

2. **配置Maven依赖**
   ```xml
   <dependencies>
       <!-- Spring Boot Web -->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
       
       <!-- MyBatis Plus -->
       <dependency>
           <groupId>com.baomidou</groupId>
           <artifactId>mybatis-plus-boot-starter</artifactId>
           <version>3.5.5</version>
       </dependency>
       
       <!-- MySQL Driver -->
       <dependency>
           <groupId>com.mysql</groupId>
           <artifactId>mysql-connector-j</artifactId>
           <scope>runtime</scope>
       </dependency>
       
       <!-- JWT -->
       <dependency>
           <groupId>io.jsonwebtoken</groupId>
           <artifactId>jjwt-api</artifactId>
           <version>0.12.3</version>
       </dependency>
       <dependency>
           <groupId>io.jsonwebtoken</groupId>
           <artifactId>jjwt-impl</artifactId>
           <version>0.12.3</version>
           <scope>runtime</scope>
       </dependency>
       <dependency>
           <groupId>io.jsonwebtoken</groupId>
           <artifactId>jjwt-jackson</artifactId>
           <version>0.12.3</version>
           <scope>runtime</scope>
       </dependency>
       
       <!-- Swagger/OpenAPI -->
       <dependency>
           <groupId>org.springdoc</groupId>
           <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
           <version>2.3.0</version>
       </dependency>
       
       <!-- Commons Lang -->
       <dependency>
           <groupId>org.apache.commons</groupId>
           <artifactId>commons-lang3</artifactId>
           <version>3.14.0</version>
       </dependency>
       
       <!-- Spring Boot Test -->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-test</artifactId>
           <scope>test</scope>
       </dependency>
   </dependencies>
   ```

3. **配置数据库连接**
   ```yaml
   # application-dev.yml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/pure_mall?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
       username: root
       password: your_password
       driver-class-name: com.mysql.cj.jdbc.Driver
   
   mybatis-plus:
     configuration:
       map-underscore-to-camel-case: true
       log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
     mapper-locations: classpath:mapper/*.xml
     type-aliases-package: com.puremall.entity
   
   # JWT配置
   jwt:
     secret: your_jwt_secret_key
     expiration: 86400000 # 24小时
   ```

### 4.2 用户模块实现

1. **创建用户实体类**
   ```java
   @Data
   @TableName("users")
   public class User {
       @TableId(type = IdType.AUTO)
       private Long id;
       private String username;
       private String password;
       private String email;
       private String phone;
       private String avatar;
       @TableField(fill = FieldFill.INSERT)
       private LocalDateTime createTime;
       private LocalDateTime lastLogin;
       private Integer status;
   }
   ```

2. **创建统一响应实体类**
   ```java
   @Data
   public class Response<T> {
       private Integer code;           // 响应码：200-成功，400-请求错误，401-未授权，500-服务器错误
       private String message;         // 响应消息
       private T data;                 // 响应数据
       
       // 成功响应
       public static <T> Response<T> success(T data) {
           Response<T> response = new Response<>();
           response.setCode(200);
           response.setMessage("success");
           response.setData(data);
           return response;
       }
       
       // 错误响应
       public static <T> Response<T> error(Integer code, String message) {
           Response<T> response = new Response<>();
           response.setCode(code);
           response.setMessage(message);
           return response;
       }
   }
   ```

3. **创建用户Mapper接口**
   ```java
   public interface UserMapper extends BaseMapper<User> {
       User findByUsername(String username);
       User findByEmail(String email);
       User findByPhone(String phone);
   }
   ```

4. **创建用户Service接口和实现**
   ```java
   public interface UserService {
       User register(User user);
       User login(User user);
       User getUserInfo(Long userId);
       User updateUserInfo(Long userId, User user);
       void logout(Long userId);
   }
   ```

5. **创建用户Service实现类**
   ```java
   @Service
   public class UserServiceImpl implements UserService {
       
       @Autowired
       private UserMapper userMapper;
       
       @Autowired
       private PasswordUtils passwordUtils;
       
       @Override
       public User register(User user) {
           // 检查用户名是否已存在
           if (userMapper.findByUsername(user.getUsername()) != null) {
               throw new BusinessException("用户名已存在");
           }
           
           // 检查邮箱是否已存在
           if (userMapper.findByEmail(user.getEmail()) != null) {
               throw new BusinessException("邮箱已存在");
           }
           
           // 检查手机号是否已存在
           if (userMapper.findByPhone(user.getPhone()) != null) {
               throw new BusinessException("手机号已存在");
           }
           
           // 密码加密
           user.setPassword(passwordUtils.encodePassword(user.getPassword()));
           
           // 设置默认头像和状态
           user.setAvatar("default_avatar.jpg");
           user.setStatus(1);
           
           // 保存用户
           userMapper.insert(user);
           
           // 返回用户信息（隐藏密码）
           user.setPassword(null);
           return user;
       }
       
       @Override
       public User login(User user) {
           // 根据用户名查询用户
           User existingUser = userMapper.findByUsername(user.getUsername());
           
           if (existingUser == null) {
               throw new BusinessException("用户名或密码错误");
           }
           
           // 验证密码
           if (!passwordUtils.verifyPassword(user.getPassword(), existingUser.getPassword())) {
               throw new BusinessException("用户名或密码错误");
           }
           
           // 更新最后登录时间
           existingUser.setLastLogin(LocalDateTime.now());
           userMapper.updateById(existingUser);
           
           // 返回用户信息（隐藏密码）
           existingUser.setPassword(null);
           return existingUser;
       }
       
       @Override
       public User getUserInfo(Long userId) {
           User user = userMapper.selectById(userId);
           if (user == null) {
               throw new BusinessException("用户不存在");
           }
           
           // 隐藏密码
           user.setPassword(null);
           return user;
       }
       
       @Override
       public User updateUserInfo(Long userId, User user) {
           User existingUser = userMapper.selectById(userId);
           if (existingUser == null) {
               throw new BusinessException("用户不存在");
           }
           
           // 更新用户信息（只更新允许修改的字段）
           existingUser.setEmail(user.getEmail());
           existingUser.setPhone(user.getPhone());
           existingUser.setAvatar(user.getAvatar());
           
           userMapper.updateById(existingUser);
           
           // 隐藏密码
           existingUser.setPassword(null);
           return existingUser;
       }
       
       @Override
       public void logout(Long userId) {
           // JWT令牌过期处理由前端负责，后端无需特殊处理
       }
   }
   ```

6. **创建用户Controller**
   ```java
   @RestController
   @RequestMapping("/api/user")
   @Tag(name = "用户管理", description = "用户注册、登录、个人信息管理接口")
   public class UserController {
       
       @Autowired
       private UserService userService;
       
       @PostMapping("/register")
       @Operation(summary = "用户注册")
       public Response<User> register(@RequestBody User user) {
           User registeredUser = userService.register(user);
           return Response.success(registeredUser);
       }
       
       @PostMapping("/login")
       @Operation(summary = "用户登录")
       public Response<User> login(@RequestBody User user) {
           User loggedInUser = userService.login(user);
           return Response.success(loggedInUser);
       }
       
       @GetMapping("/info")
       @Operation(summary = "获取用户信息")
       public Response<User> getUserInfo() {
           // 从JWT中获取用户ID
           Long userId = getCurrentUserId();
           User user = userService.getUserInfo(userId);
           return Response.success(user);
       }
       
       @PutMapping("/info")
       @Operation(summary = "更新用户信息")
       public Response<User> updateUserInfo(@RequestBody User user) {
           Long userId = getCurrentUserId();
           User updatedUser = userService.updateUserInfo(userId, user);
           return Response.success(updatedUser);
       }
       
       @PostMapping("/logout")
       @Operation(summary = "用户登出")
       public Response<Void> logout() {
           Long userId = getCurrentUserId();
           userService.logout(userId);
           return Response.success(null);
       }
       
       // 从JWT中获取当前用户ID的工具方法
       private Long getCurrentUserId() {
           // 实际开发中，这里应该从SecurityContextHolder或请求头中获取用户ID
           // 这里为了简化示例，直接返回1
           return 1L;
       }
   }
   ```

5. **实现JWT认证**
   - 创建JwtUtils工具类
   - 创建JWT过滤器
   - 配置Spring Security

### 4.3 商品模块实现

1. **创建商品相关实体类**
   
   **Product.java**
   ```java
   @Data
   @TableName("products")
   public class Product {
       @TableId(type = IdType.AUTO)
       private Long id;
       private String name;
       private String brief;
       private BigDecimal price;
       private BigDecimal originalPrice;
       private Integer sales;
       private Long categoryId;
       private String detail;
       private Integer status;
       @TableField(fill = FieldFill.INSERT)
       private LocalDateTime createTime;
       @TableField(fill = FieldFill.INSERT_UPDATE)
       private LocalDateTime updateTime;
   }
   ```
   
   **ProductSpec.java**
   ```java
   @Data
   @TableName("product_specs")
   public class ProductSpec {
       @TableId(type = IdType.AUTO)
       private Long id;
       private Long productId;
       private String name;
       private BigDecimal price;
       private Integer stock;
       @TableField(fill = FieldFill.INSERT)
       private LocalDateTime createTime;
   }
   ```
   
   **ProductParam.java**
   ```java
   @Data
   @TableName("product_params")
   public class ProductParam {
       @TableId(type = IdType.AUTO)
       private Long id;
       private Long productId;
       private String paramName;
       private String paramValue;
   }
   ```
   
   **ProductImage.java**
   ```java
   @Data
   @TableName("product_images")
   public class ProductImage {
       @TableId(type = IdType.AUTO)
       private Long id;
       private Long productId;
       private String imageUrl;
       private Integer sort;
   }
   ```
   
   **ProductReview.java**
   ```java
   @Data
   @TableName("product_reviews")
   public class ProductReview {
       @TableId(type = IdType.AUTO)
       private Long id;
       private Long productId;
       private Long userId;
       private Integer rating;
       private String content;
       @TableField(fill = FieldFill.INSERT)
       private LocalDateTime createTime;
   }
   ```

2. **创建商品Mapper接口**
   ```java
   public interface ProductMapper extends BaseMapper<Product> {
       List<Product> getProductsByCategory(Long categoryId);
   }
   ```

3. **创建商品规格Mapper接口**
   ```java
   public interface ProductSpecMapper extends BaseMapper<ProductSpec> {
       List<ProductSpec> getProductSpecsByProductId(Long productId);
   }
   ```

4. **创建商品参数Mapper接口**
   ```java
   public interface ProductParamMapper extends BaseMapper<ProductParam> {
       List<ProductParam> getProductParamsByProductId(Long productId);
   }
   ```

5. **创建商品图片Mapper接口**
   ```java
   public interface ProductImageMapper extends BaseMapper<ProductImage> {
       List<ProductImage> getProductImagesByProductId(Long productId);
   }
   ```

6. **创建商品评论Mapper接口**
   ```java
   public interface ProductReviewMapper extends BaseMapper<ProductReview> {
       List<ProductReview> getProductReviewsByProductId(Long productId);
   }
   ```

7. **创建商品Service接口**
   ```java
   public interface ProductService {
       List<Product> getProductList();
       List<Product> getProductsByCategory(Long categoryId);
       Product getProductById(Long productId);
       List<ProductSpec> getProductSpecs(Long productId);
       List<ProductParam> getProductParams(Long productId);
       List<ProductImage> getProductImages(Long productId);
       List<ProductReview> getProductReviews(Long productId);
   }
   ```

8. **创建商品Service实现类**
   ```java
   @Service
   public class ProductServiceImpl implements ProductService {
       
       @Autowired
       private ProductMapper productMapper;
       
       @Autowired
       private ProductSpecMapper productSpecMapper;
       
       @Autowired
       private ProductParamMapper productParamMapper;
       
       @Autowired
       private ProductImageMapper productImageMapper;
       
       @Autowired
       private ProductReviewMapper productReviewMapper;
       
       @Override
       public List<Product> getProductList() {
           // 查询所有上架商品
           QueryWrapper<Product> wrapper = new QueryWrapper<>();
           wrapper.eq("status", 1);
           return productMapper.selectList(wrapper);
       }
       
       @Override
       public List<Product> getProductsByCategory(Long categoryId) {
           // 查询指定分类下的上架商品
           QueryWrapper<Product> wrapper = new QueryWrapper<>();
           wrapper.eq("category_id", categoryId)
                  .eq("status", 1);
           return productMapper.selectList(wrapper);
       }
       
       @Override
       public Product getProductById(Long productId) {
           Product product = productMapper.selectById(productId);
           if (product == null || product.getStatus() != 1) {
               throw new BusinessException("商品不存在或已下架");
           }
           return product;
       }
       
       @Override
       public List<ProductSpec> getProductSpecs(Long productId) {
           return productSpecMapper.getProductSpecsByProductId(productId);
       }
       
       @Override
       public List<ProductParam> getProductParams(Long productId) {
           return productParamMapper.getProductParamsByProductId(productId);
       }
       
       @Override
       public List<ProductImage> getProductImages(Long productId) {
           return productImageMapper.getProductImagesByProductId(productId);
       }
       
       @Override
       public List<ProductReview> getProductReviews(Long productId) {
           return productReviewMapper.getProductReviewsByProductId(productId);
       }
   }
   ```

9. **创建商品Controller**
   ```java
   @RestController
   @RequestMapping("/api/product")
   @Tag(name = "商品管理", description = "商品列表、详情、评论接口")
   public class ProductController {
       
       @Autowired
       private ProductService productService;
       
       @GetMapping("/list")
       @Operation(summary = "获取商品列表")
       public Response<List<Product>> getProductList() {
           List<Product> products = productService.getProductList();
           return Response.success(products);
       }
       
       @GetMapping("/category/{categoryId}")
       @Operation(summary = "按分类获取商品")
       public Response<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
           List<Product> products = productService.getProductsByCategory(categoryId);
           return Response.success(products);
       }
       
       @GetMapping("/detail/{productId}")
       @Operation(summary = "获取商品详情")
       public Response<Map<String, Object>> getProductDetail(@PathVariable Long productId) {
           // 获取商品基本信息
           Product product = productService.getProductById(productId);
           
           // 获取商品规格
           List<ProductSpec> specs = productService.getProductSpecs(productId);
           
           // 获取商品参数
           List<ProductParam> params = productService.getProductParams(productId);
           
           // 获取商品图片
           List<ProductImage> images = productService.getProductImages(productId);
           
           // 组装返回数据
           Map<String, Object> result = new HashMap<>();
           result.put("product", product);
           result.put("specs", specs);
           result.put("params", params);
           result.put("images", images);
           
           return Response.success(result);
       }
       
       @GetMapping("/{productId}/reviews")
       @Operation(summary = "获取商品评论")
       public Response<List<ProductReview>> getProductReviews(@PathVariable Long productId) {
           List<ProductReview> reviews = productService.getProductReviews(productId);
           return Response.success(reviews);
       }
   }
   ```

### 4.4 购物车模块实现

1. **创建购物车相关实体类**
   
   **Cart.java**
   ```java
   @Data
   @TableName("carts")
   public class Cart {
       @TableId(type = IdType.AUTO)
       private Long id;
       private Long userId;
       @TableField(fill = FieldFill.INSERT)
       private LocalDateTime createTime;
       @TableField(fill = FieldFill.INSERT_UPDATE)
       private LocalDateTime updateTime;
   }
   ```
   
   **CartItem.java**
   ```java
   @Data
   @TableName("cart_items")
   public class CartItem {
       @TableId(type = IdType.AUTO)
       private Long id;
       private Long cartId;
       private Long productId;
       private Long specId;
       private Integer quantity;
       private Integer selected;
       private BigDecimal price;
       private String productName;
       private String specName;
       private String productImage;
       @TableField(fill = FieldFill.INSERT)
       private LocalDateTime createTime;
       @TableField(fill = FieldFill.INSERT_UPDATE)
       private LocalDateTime updateTime;
   }
   ```

2. **创建购物车Mapper接口**
   ```java
   public interface CartMapper extends BaseMapper<Cart> {
       Cart getCartByUserId(Long userId);
   }
   ```

3. **创建购物车项Mapper接口**
   ```java
   public interface CartItemMapper extends BaseMapper<CartItem> {
       List<CartItem> getCartItemsByCartId(Long cartId);
   }
   ```

4. **创建购物车Service接口**
   ```java
   public interface CartService {
       Cart getCart(Long userId);
       List<CartItem> getCartItems(Long cartId);
       void addToCart(Long userId, CartItem cartItem);
       void updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity);
       void deleteCartItem(Long userId, Long cartItemId);
       void toggleCartItemSelected(Long userId, Long cartItemId);
       void toggleAllCartItemsSelected(Long userId, Boolean selected);
   }
   ```

5. **创建购物车Service实现类**
   ```java
   @Service
   public class CartServiceImpl implements CartService {
       
       @Autowired
       private CartMapper cartMapper;
       
       @Autowired
       private CartItemMapper cartItemMapper;
       
       @Autowired
       private ProductSpecMapper productSpecMapper;
       
       @Override
       public Cart getCart(Long userId) {
           // 获取用户购物车，不存在则创建
           Cart cart = cartMapper.getCartByUserId(userId);
           if (cart == null) {
               cart = new Cart();
               cart.setUserId(userId);
               cartMapper.insert(cart);
           }
           return cart;
       }
       
       @Override
       public List<CartItem> getCartItems(Long cartId) {
           return cartItemMapper.getCartItemsByCartId(cartId);
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
           Cart cart = cartMapper.getCartByUserId(userId);
           if (cart == null) {
               return;
           }
           
           // 更新所有购物车项的选中状态
           QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
           wrapper.eq("cart_id", cart.getId());
           List<CartItem> cartItems = cartItemMapper.selectList(wrapper);
           
           for (CartItem item : cartItems) {
               item.setSelected(selected ? 1 : 0);
               cartItemMapper.updateById(item);
           }
       }
   }
   ```

6. **创建购物车Controller**
   ```java
   @RestController
   @RequestMapping("/api/cart")
   @Tag(name = "购物车管理", description = "购物车添加、修改、删除接口")
   public class CartController {
       
       @Autowired
       private CartService cartService;
       
       @GetMapping("/")
       @Operation(summary = "获取购物车")
       public Response<Map<String, Object>> getCart(@RequestParam Long userId) {
           Cart cart = cartService.getCart(userId);
           List<CartItem> items = cartService.getCartItems(cart.getId());
           
           Map<String, Object> result = new HashMap<>();
           result.put("cart", cart);
           result.put("items", items);
           
           return Response.success(result);
       }
       
       @PostMapping("/")
       @Operation(summary = "添加商品到购物车")
       public Response<Void> addToCart(@RequestParam Long userId, @RequestBody CartItem cartItem) {
           cartService.addToCart(userId, cartItem);
           return Response.success();
       }
       
       @PutMapping("/item/{cartItemId}/quantity")
       @Operation(summary = "修改购物车项数量")
       public Response<Void> updateCartItemQuantity(@RequestParam Long userId, @PathVariable Long cartItemId, @RequestParam Integer quantity) {
           cartService.updateCartItemQuantity(userId, cartItemId, quantity);
           return Response.success();
       }
       
       @PutMapping("/item/{cartItemId}/selected")
       @Operation(summary = "切换购物车项选中状态")
       public Response<Void> toggleCartItemSelected(@RequestParam Long userId, @PathVariable Long cartItemId) {
           cartService.toggleCartItemSelected(userId, cartItemId);
           return Response.success();
       }
       
       @PutMapping("/selected")
       @Operation(summary = "切换所有购物车项选中状态")
       public Response<Void> toggleAllCartItemsSelected(@RequestParam Long userId, @RequestParam Boolean selected) {
           cartService.toggleAllCartItemsSelected(userId, selected);
           return Response.success();
       }
       
       @DeleteMapping("/item/{cartItemId}")
       @Operation(summary = "删除购物车项")
       public Response<Void> deleteCartItem(@RequestParam Long userId, @PathVariable Long cartItemId) {
           cartService.deleteCartItem(userId, cartItemId);
           return Response.success();
       }
   }
   ```

### 4.5 订单模块实现

1. **创建订单相关实体类**
   
   **Order.java**
   ```java
   @Data
   @TableName("orders")
   public class Order {
       @TableId(type = IdType.AUTO)
       private Long id;
       private Long userId;
       private String orderNo;
       private BigDecimal totalAmount;
       private Integer status;
       private String address;
       private String phone;
       private String consignee;
       private String remark;
       @TableField(fill = FieldFill.INSERT)
       private LocalDateTime createTime;
       @TableField(fill = FieldFill.INSERT_UPDATE)
       private LocalDateTime updateTime;
   }
   ```
   
   **OrderItem.java**
   ```java
   @Data
   @TableName("order_items")
   public class OrderItem {
       @TableId(type = IdType.AUTO)
       private Long id;
       private Long orderId;
       private Long productId;
       private Long specId;
       private Integer quantity;
       private BigDecimal price;
       private String productName;
       private String specName;
       private String productImage;
       @TableField(fill = FieldFill.INSERT)
       private LocalDateTime createTime;
   }
   ```

2. **创建订单Mapper接口**
   ```java
   public interface OrderMapper extends BaseMapper<Order> {
       List<Order> getOrdersByUserId(Long userId);
       Order getOrderDetail(Long orderId);
   }
   ```

3. **创建订单项Mapper接口**
   ```java
   public interface OrderItemMapper extends BaseMapper<OrderItem> {
       List<OrderItem> getOrderItemsByOrderId(Long orderId);
   }
   ```

4. **创建订单Service接口**
   ```java
   public interface OrderService {
       Order createOrder(Long userId, Order order, List<OrderItem> orderItems);
       Order getOrderDetail(Long userId, Long orderId);
       List<Order> getOrderList(Long userId);
       List<OrderItem> getOrderItems(Long orderId);
       void cancelOrder(Long userId, Long orderId);
       void completePayment(Long userId, Long orderId);
   }
   ```

5. **创建订单Service实现类**
   ```java
   @Service
   public class OrderServiceImpl implements OrderService {
       
       @Autowired
       private OrderMapper orderMapper;
       
       @Autowired
       private OrderItemMapper orderItemMapper;
       
       @Autowired
       private CartService cartService;
       
       @Autowired
       private ProductSpecMapper productSpecMapper;
       
       @Override
       public Order createOrder(Long userId, Order order, List<OrderItem> orderItems) {
           // 生成订单号
           String orderNo = "ORD" + System.currentTimeMillis() + userId;
           order.setOrderNo(orderNo);
           order.setUserId(userId);
           order.setStatus(0); // 待付款状态
           
           // 计算订单总金额
           BigDecimal totalAmount = BigDecimal.ZERO;
           for (OrderItem item : orderItems) {
               // 验证商品规格是否存在
               ProductSpec spec = productSpecMapper.selectById(item.getSpecId());
               if (spec == null) {
                   throw new BusinessException("商品规格不存在");
               }
               
               // 检查库存是否足够
               if (item.getQuantity() > spec.getStock()) {
                   throw new BusinessException(item.getProductName() + "库存不足");
               }
               
               // 设置商品价格
               item.setPrice(spec.getPrice());
               totalAmount = totalAmount.add(spec.getPrice().multiply(new BigDecimal(item.getQuantity())));
               
               // 更新库存
               spec.setStock(spec.getStock() - item.getQuantity());
               productSpecMapper.updateById(spec);
           }
           
           order.setTotalAmount(totalAmount);
           
           // 保存订单
           orderMapper.insert(order);
           
           // 保存订单项
           for (OrderItem item : orderItems) {
               item.setOrderId(order.getId());
               orderItemMapper.insert(item);
           }
           
           // 清空购物车中已购买的商品
           cartService.clearCart(userId);
           
           return order;
       }
       
       @Override
       public Order getOrderDetail(Long userId, Long orderId) {
           Order order = orderMapper.selectById(orderId);
           if (order == null) {
               throw new BusinessException("订单不存在");
           }
           
           // 验证订单是否属于当前用户
           if (!order.getUserId().equals(userId)) {
               throw new BusinessException("无权查看该订单");
           }
           
           return order;
       }
       
       @Override
       public List<Order> getOrderList(Long userId) {
           return orderMapper.getOrdersByUserId(userId);
       }
       
       @Override
       public List<OrderItem> getOrderItems(Long orderId) {
           return orderItemMapper.getOrderItemsByOrderId(orderId);
       }
       
       @Override
       public void cancelOrder(Long userId, Long orderId) {
           Order order = orderMapper.selectById(orderId);
           if (order == null) {
               throw new BusinessException("订单不存在");
           }
           
           // 验证订单是否属于当前用户
           if (!order.getUserId().equals(userId)) {
               throw new BusinessException("无权操作该订单");
           }
           
           // 只能取消待付款的订单
           if (order.getStatus() != 0) {
               throw new BusinessException("该订单状态不允许取消");
           }
           
           // 更新订单状态
           order.setStatus(4); // 已取消状态
           orderMapper.updateById(order);
           
           // 恢复库存
           List<OrderItem> orderItems = orderItemMapper.getOrderItemsByOrderId(orderId);
           for (OrderItem item : orderItems) {
               ProductSpec spec = productSpecMapper.selectById(item.getSpecId());
               if (spec != null) {
                   spec.setStock(spec.getStock() + item.getQuantity());
                   productSpecMapper.updateById(spec);
               }
           }
       }
       
       @Override
       public void completePayment(Long userId, Long orderId) {
           Order order = orderMapper.selectById(orderId);
           if (order == null) {
               throw new BusinessException("订单不存在");
           }
           
           // 验证订单是否属于当前用户
           if (!order.getUserId().equals(userId)) {
               throw new BusinessException("无权操作该订单");
           }
           
           // 只能支付待付款的订单
           if (order.getStatus() != 0) {
               throw new BusinessException("该订单状态不允许支付");
           }
           
           // 更新订单状态
           order.setStatus(1); // 已付款状态
           orderMapper.updateById(order);
       }
   }
   ```

6. **创建订单Controller**
   ```java
   @RestController
   @RequestMapping("/api/order")
   @Tag(name = "订单管理", description = "订单创建、查询、取消接口")
   public class OrderController {
       
       @Autowired
       private OrderService orderService;
       
       @PostMapping("/")
       @Operation(summary = "创建订单")
       public Response<Order> createOrder(@RequestParam Long userId, @RequestBody Map<String, Object> orderData) {
           Order order = new Order();
           order.setAddress((String) orderData.get("address"));
           order.setPhone((String) orderData.get("phone"));
           order.setConsignee((String) orderData.get("consignee"));
           order.setRemark((String) orderData.get("remark"));
           
           List<OrderItem> orderItems = new ArrayList<>();
           List<Map<String, Object>> items = (List<Map<String, Object>>) orderData.get("items");
           for (Map<String, Object> itemMap : items) {
               OrderItem item = new OrderItem();
               item.setProductId(((Number) itemMap.get("productId")).longValue());
               item.setSpecId(((Number) itemMap.get("specId")).longValue());
               item.setQuantity(((Number) itemMap.get("quantity")).intValue());
               item.setProductName((String) itemMap.get("productName"));
               item.setSpecName((String) itemMap.get("specName"));
               item.setProductImage((String) itemMap.get("productImage"));
               orderItems.add(item);
           }
           
           Order createdOrder = orderService.createOrder(userId, order, orderItems);
           return Response.success(createdOrder);
       }
       
       @GetMapping("/")
       @Operation(summary = "获取订单列表")
       public Response<List<Order>> getOrderList(@RequestParam Long userId) {
           List<Order> orders = orderService.getOrderList(userId);
           return Response.success(orders);
       }
       
       @GetMapping("/{orderId}")
       @Operation(summary = "获取订单详情")
       public Response<Map<String, Object>> getOrderDetail(@RequestParam Long userId, @PathVariable Long orderId) {
           Order order = orderService.getOrderDetail(userId, orderId);
           List<OrderItem> items = orderService.getOrderItems(orderId);
           
           Map<String, Object> result = new HashMap<>();
           result.put("order", order);
           result.put("items", items);
           
           return Response.success(result);
       }
       
       @PutMapping("/{orderId}/cancel")
       @Operation(summary = "取消订单")
       public Response<Void> cancelOrder(@RequestParam Long userId, @PathVariable Long orderId) {
           orderService.cancelOrder(userId, orderId);
           return Response.success();
       }
       
       @PutMapping("/{orderId}/pay")
       @Operation(summary = "完成支付")
       public Response<Void> completePayment(@RequestParam Long userId, @PathVariable Long orderId) {
           orderService.completePayment(userId, orderId);
           return Response.success();
       }
   }
   ```

### 4.6 收藏夹模块实现

1. **创建收藏夹相关实体类**
   
   **Wishlist.java**
   ```java
   @Data
   @TableName("wishlists")
   public class Wishlist {
       @TableId(type = IdType.AUTO)
       private Long id;
       private Long userId;
       @TableField(fill = FieldFill.INSERT)
       private LocalDateTime createTime;
       @TableField(fill = FieldFill.INSERT_UPDATE)
       private LocalDateTime updateTime;
   }
   ```
   
   **WishlistItem.java**
   ```java
   @Data
   @TableName("wishlist_items")
   public class WishlistItem {
       @TableId(type = IdType.AUTO)
       private Long id;
       private Long wishlistId;
       private Long productId;
       private String productName;
       private BigDecimal price;
       private String productImage;
       @TableField(fill = FieldFill.INSERT)
       private LocalDateTime createTime;
   }
   ```

2. **创建收藏夹Mapper接口**
   ```java
   public interface WishlistMapper extends BaseMapper<Wishlist> {
       Wishlist getWishlistByUserId(Long userId);
   }
   ```

3. **创建收藏夹项Mapper接口**
   ```java
   public interface WishlistItemMapper extends BaseMapper<WishlistItem> {
       List<WishlistItem> getWishlistItemsByWishlistId(Long wishlistId);
       WishlistItem getWishlistItemByProductId(Long wishlistId, Long productId);
   }
   ```

4. **创建收藏夹Service接口**
   ```java
   public interface WishlistService {
       Wishlist getWishlist(Long userId);
       List<WishlistItem> getWishlistItems(Long wishlistId);
       void addToWishlist(Long userId, Long productId);
       void removeFromWishlist(Long userId, Long wishlistItemId);
   }
   ```

5. **创建收藏夹Service实现类**
   ```java
   @Service
   public class WishlistServiceImpl implements WishlistService {
       
       @Autowired
       private WishlistMapper wishlistMapper;
       
       @Autowired
       private WishlistItemMapper wishlistItemMapper;
       
       @Autowired
       private ProductMapper productMapper;
       
       @Override
       public Wishlist getWishlist(Long userId) {
           // 获取用户收藏夹，不存在则创建
           Wishlist wishlist = wishlistMapper.getWishlistByUserId(userId);
           if (wishlist == null) {
               wishlist = new Wishlist();
               wishlist.setUserId(userId);
               wishlistMapper.insert(wishlist);
           }
           return wishlist;
       }
       
       @Override
       public List<WishlistItem> getWishlistItems(Long wishlistId) {
           return wishlistItemMapper.getWishlistItemsByWishlistId(wishlistId);
       }
       
       @Override
       public void addToWishlist(Long userId, Long productId) {
           // 获取用户收藏夹
           Wishlist wishlist = getWishlist(userId);
           
           // 验证商品是否存在
           Product product = productMapper.selectById(productId);
           if (product == null || product.getStatus() != 1) {
               throw new BusinessException("商品不存在或已下架");
           }
           
           // 检查商品是否已在收藏夹中
           WishlistItem existingItem = wishlistItemMapper.getWishlistItemByProductId(wishlist.getId(), productId);
           if (existingItem != null) {
               throw new BusinessException("该商品已在收藏夹中");
           }
           
           // 添加到收藏夹
           WishlistItem wishlistItem = new WishlistItem();
           wishlistItem.setWishlistId(wishlist.getId());
           wishlistItem.setProductId(productId);
           wishlistItem.setProductName(product.getName());
           wishlistItem.setPrice(product.getPrice());
           wishlistItem.setProductImage("默认图片URL"); // 实际应用中应从商品图片表获取
           wishlistItemMapper.insert(wishlistItem);
       }
       
       @Override
       public void removeFromWishlist(Long userId, Long wishlistItemId) {
           // 验证收藏夹项是否存在
           WishlistItem wishlistItem = wishlistItemMapper.selectById(wishlistItemId);
           if (wishlistItem == null) {
               throw new BusinessException("收藏夹项不存在");
           }
           
           // 验证该收藏夹项是否属于当前用户
           Wishlist wishlist = wishlistMapper.selectById(wishlistItem.getWishlistId());
           if (!wishlist.getUserId().equals(userId)) {
               throw new BusinessException("无权操作该收藏夹项");
           }
           
           // 从收藏夹删除
           wishlistItemMapper.deleteById(wishlistItemId);
       }
   }
   ```

6. **创建收藏夹Controller**
   ```java
   @RestController
   @RequestMapping("/api/wishlist")
   @Tag(name = "收藏夹管理", description = "收藏夹添加、删除接口")
   public class WishlistController {
       
       @Autowired
       private WishlistService wishlistService;
       
       @GetMapping("/")
       @Operation(summary = "获取收藏夹")
       public Response<Map<String, Object>> getWishlist(@RequestParam Long userId) {
           Wishlist wishlist = wishlistService.getWishlist(userId);
           List<WishlistItem> items = wishlistService.getWishlistItems(wishlist.getId());
           
           Map<String, Object> result = new HashMap<>();
           result.put("wishlist", wishlist);
           result.put("items", items);
           
           return Response.success(result);
       }
       
       @PostMapping("/")
       @Operation(summary = "添加商品到收藏夹")
       public Response<Void> addToWishlist(@RequestParam Long userId, @RequestParam Long productId) {
           wishlistService.addToWishlist(userId, productId);
           return Response.success();
       }
       
       @DeleteMapping("/item/{wishlistItemId}")
       @Operation(summary = "从收藏夹删除商品")
       public Response<Void> removeFromWishlist(@RequestParam Long userId, @PathVariable Long wishlistItemId) {
           wishlistService.removeFromWishlist(userId, wishlistItemId);
           return Response.success();
       }
   }
   ```

## 5. API文档和测试

1. **配置Swagger/OpenAPI**
   ```java
   @Configuration
   public class SwaggerConfig {
       @Bean
       public OpenAPI pureMallOpenAPI() {
           return new OpenAPI()
                   .info(new Info().title("Pure Mall API")
                           .description("Pure Mall 电商平台API文档")
                           .version("v1.0")
                           .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                   .externalDocs(new ExternalDocumentation()
                           .description("Pure Mall Documentation")
                           .url("https://springshop.wiki.github.org/docs"));
       }
   }
   ```

2. **编写单元测试**
   - 用户服务测试
   - 商品服务测试
   - 订单服务测试

3. **API测试**
   - 使用Postman或Swagger UI测试所有API接口
   - 验证接口的正确性和稳定性

## 6. 部署和维护

1. **打包项目**
   ```bash
   mvn clean package -DskipTests
   ```

2. **部署到服务器**
   - 将jar包上传到服务器
   - 配置application-prod.yml
   - 启动应用

3. **监控和维护**
   - 配置日志收集
   - 定期备份数据库
   - 监控服务器性能

## 7. 注意事项

1. **安全性**
   - 使用HTTPS协议
   - 密码加密存储(BCrypt)
   - 防止SQL注入
   - 防止XSS攻击
   - 接口权限控制

2. **性能优化**
   - 数据库索引优化
   - 接口响应缓存
   - 图片资源CDN加速

3. **扩展性**
   - 模块化设计
   - 接口版本控制
   - 微服务架构考虑

4. **数据一致性**
   - 购物车和库存的一致性
   - 订单和支付的一致性
   - 使用事务管理

以上是详细的后端+数据库任务清单，按照这个清单可以逐步实现完整的后端系统。