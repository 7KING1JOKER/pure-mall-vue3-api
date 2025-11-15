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
│   │           │   ├── CartMapper.java            # 购物车Mapper
│   │           │   ├── WishlistMapper.java        # 收藏夹Mapper
│   │           │   └── OrderMapper.java           # 订单Mapper
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
│   │           │   └── OrderItem.java             # 订单项实体
│   │           ├── dto/                           # 数据传输对象
│   │           │   ├── LoginDto.java              # 登录请求
│   │           │   ├── RegisterDto.java           # 注册请求
│   │           │   ├── AddressDto.java            # 地址请求
│   │           │   ├── ProductDto.java            # 商品请求
│   │           │   ├── CartItemDto.java           # 购物车项请求
│   │           │   ├── OrderDto.java              # 订单请求
│   │           │   └── ResponseDto.java           # 统一响应
│   │           ├── vo/                            # 视图对象
│   │           │   ├── UserVo.java                # 用户响应
│   │           │   ├── ProductVo.java             # 商品响应
│   │           │   ├── CartVo.java                # 购物车响应
│   │           │   ├── OrderVo.java               # 订单响应
│   │           │   └── WishlistVo.java            # 收藏夹响应
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
│           ├── CartMapper.xml                     # 购物车Mapper映射
│           └── OrderMapper.xml                    # 订单Mapper映射
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

2. **创建用户Mapper接口**
   ```java
   public interface UserMapper extends BaseMapper<User> {
       User findByUsername(String username);
       User findByEmail(String email);
       User findByPhone(String phone);
   }
   ```

3. **创建用户Service接口和实现**
   ```java
   public interface UserService {
       User register(RegisterDto registerDto);
       User login(LoginDto loginDto);
       User getUserInfo(Long userId);
       User updateUserInfo(Long userId, User user);
       void logout(Long userId);
   }
   ```

4. **创建用户Controller**
   ```java
   @RestController
   @RequestMapping("/api/user")
   @Tag(name = "用户管理", description = "用户注册、登录、个人信息管理接口")
   public class UserController {
       @PostMapping("/register")
       @Operation(summary = "用户注册")
       public ResponseDto<UserVo> register(@RequestBody RegisterDto registerDto) {
           // 实现注册逻辑
       }
       
       @PostMapping("/login")
       @Operation(summary = "用户登录")
       public ResponseDto<UserVo> login(@RequestBody LoginDto loginDto) {
           // 实现登录逻辑
       }
       
       @GetMapping("/info")
       @Operation(summary = "获取用户信息")
       public ResponseDto<UserVo> getUserInfo() {
           // 实现获取用户信息逻辑
       }
   }
   ```

5. **实现JWT认证**
   - 创建JwtUtils工具类
   - 创建JWT过滤器
   - 配置Spring Security

### 4.3 商品模块实现

1. **创建商品相关实体类**
   - Product
   - ProductSpec
   - ProductParam
   - ProductImage
   - ProductReview

2. **创建商品Mapper接口**
   ```java
   public interface ProductMapper extends BaseMapper<Product> {
       List<Product> getProductsByCategory(Long categoryId);
       Product getProductDetail(Long productId);
   }
   ```

3. **创建商品Service接口和实现**
   ```java
   public interface ProductService {
       List<ProductVo> getProductList();
       List<ProductVo> getProductsByCategory(Long categoryId);
       ProductVo getProductDetail(Long productId);
       List<ProductReviewVo> getProductReviews(Long productId);
   }
   ```

4. **创建商品Controller**
   ```java
   @RestController
   @RequestMapping("/api/product")
   @Tag(name = "商品管理", description = "商品列表、详情、评论接口")
   public class ProductController {
       @GetMapping("/list")
       @Operation(summary = "获取商品列表")
       public ResponseDto<List<ProductVo>> getProductList() {
           // 实现获取商品列表逻辑
       }
       
       @GetMapping("/category/{categoryId}")
       @Operation(summary = "按分类获取商品")
       public ResponseDto<List<ProductVo>> getProductsByCategory(@PathVariable Long categoryId) {
           // 实现按分类获取商品逻辑
       }
       
       @GetMapping("/detail/{productId}")
       @Operation(summary = "获取商品详情")
       public ResponseDto<ProductVo> getProductDetail(@PathVariable Long productId) {
           // 实现获取商品详情逻辑
       }
   }
   ```

### 4.4 购物车模块实现

1. **创建购物车相关实体类**
   - Cart
   - CartItem

2. **创建购物车Mapper接口**
   ```java
   public interface CartMapper extends BaseMapper<Cart> {
       Cart getCartByUserId(Long userId);
   }
   
   public interface CartItemMapper extends BaseMapper<CartItem> {
       List<CartItem> getCartItemsByCartId(Long cartId);
   }
   ```

3. **创建购物车Service接口和实现**
   ```java
   public interface CartService {
       CartVo getCart(Long userId);
       void addToCart(Long userId, CartItemDto cartItemDto);
       void updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity);
       void deleteCartItem(Long userId, Long cartItemId);
       void toggleCartItemSelected(Long userId, Long cartItemId);
       void toggleAllCartItemsSelected(Long userId, Boolean selected);
   }
   ```

4. **创建购物车Controller**
   ```java
   @RestController
   @RequestMapping("/api/cart")
   @Tag(name = "购物车管理", description = "购物车添加、修改、删除接口")
   public class CartController {
       @GetMapping("/")
       @Operation(summary = "获取购物车")
       public ResponseDto<CartVo> getCart() {
           // 实现获取购物车逻辑
       }
       
       @PostMapping("/")
       @Operation(summary = "添加商品到购物车")
       public ResponseDto<Void> addToCart(@RequestBody CartItemDto cartItemDto) {
           // 实现添加商品到购物车逻辑
       }
       
       @PutMapping("/item/{cartItemId}/quantity")
       @Operation(summary = "修改购物车项数量")
       public ResponseDto<Void> updateCartItemQuantity(@PathVariable Long cartItemId, @RequestParam Integer quantity) {
           // 实现修改购物车项数量逻辑
       }
   }
   ```

### 4.5 订单模块实现

1. **创建订单相关实体类**
   - Order
   - OrderItem

2. **创建订单Mapper接口**
   ```java
   public interface OrderMapper extends BaseMapper<Order> {
       List<Order> getOrdersByUserId(Long userId);
       Order getOrderDetail(Long orderId);
   }
   
   public interface OrderItemMapper extends BaseMapper<OrderItem> {
       List<OrderItem> getOrderItemsByOrderId(Long orderId);
   }
   ```

3. **创建订单Service接口和实现**
   ```java
   public interface OrderService {
       OrderVo createOrder(Long userId, OrderDto orderDto);
       OrderVo getOrderDetail(Long userId, Long orderId);
       List<OrderVo> getOrderList(Long userId);
       void cancelOrder(Long userId, Long orderId);
       void completePayment(Long userId, Long orderId);
   }
   ```

4. **创建订单Controller**
   ```java
   @RestController
   @RequestMapping("/api/order")
   @Tag(name = "订单管理", description = "订单创建、查询、取消接口")
   public class OrderController {
       @PostMapping("/")
       @Operation(summary = "创建订单")
       public ResponseDto<OrderVo> createOrder(@RequestBody OrderDto orderDto) {
           // 实现创建订单逻辑
       }
       
       @GetMapping("/")
       @Operation(summary = "获取订单列表")
       public ResponseDto<List<OrderVo>> getOrderList() {
           // 实现获取订单列表逻辑
       }
       
       @GetMapping("/{orderId}")
       @Operation(summary = "获取订单详情")
       public ResponseDto<OrderVo> getOrderDetail(@PathVariable Long orderId) {
           // 实现获取订单详情逻辑
       }
   }
   ```

### 4.6 收藏夹模块实现

1. **创建收藏夹相关实体类**
   - Wishlist
   - WishlistItem

2. **创建收藏夹Mapper接口**
   ```java
   public interface WishlistMapper extends BaseMapper<Wishlist> {
       Wishlist getWishlistByUserId(Long userId);
   }
   
   public interface WishlistItemMapper extends BaseMapper<WishlistItem> {
       List<WishlistItem> getWishlistItemsByWishlistId(Long wishlistId);
   }
   ```

3. **创建收藏夹Service接口和实现**
   ```java
   public interface WishlistService {
       WishlistVo getWishlist(Long userId);
       void addToWishlist(Long userId, Long productId);
       void removeFromWishlist(Long userId, Long wishlistItemId);
   }
   ```

4. **创建收藏夹Controller**
   ```java
   @RestController
   @RequestMapping("/api/wishlist")
   @Tag(name = "收藏夹管理", description = "收藏夹添加、删除接口")
   public class WishlistController {
       @GetMapping("/")
       @Operation(summary = "获取收藏夹")
       public ResponseDto<WishlistVo> getWishlist() {
           // 实现获取收藏夹逻辑
       }
       
       @PostMapping("/")
       @Operation(summary = "添加商品到收藏夹")
       public ResponseDto<Void> addToWishlist(@RequestParam Long productId) {
           // 实现添加商品到收藏夹逻辑
       }
       
       @DeleteMapping("/item/{wishlistItemId}")
       @Operation(summary = "从收藏夹删除商品")
       public ResponseDto<Void> removeFromWishlist(@PathVariable Long wishlistItemId) {
           // 实现从收藏夹删除商品逻辑
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