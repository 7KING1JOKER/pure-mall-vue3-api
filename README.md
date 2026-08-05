# Pure Mall Backend（纯商城后端）

> Pure Mall 电商平台后端服务，基于 Spring Boot + MyBatis Plus + MySQL 构建，提供商品、购物车、订单、用户、地址、收藏夹等电商核心业务接口，并采用 JWT 双 Token 机制实现无感刷新认证。
>
> 本文档基于当前 `local` 分支代码扫描生成。

---

## 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [功能模块](#功能模块)
- [数据库设计](#数据库设计)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [API 接口概览](#api-接口概览)
- [认证机制（JWT 双 Token）](#认证机制jwt-双-token)
- [统一响应与异常处理](#统一响应与异常处理)
- [默认账号](#默认账号)

---

## 项目简介

Pure Mall Backend 是一个前后端分离电商项目的后端部分，负责为前端提供 RESTful API。系统覆盖了电商主流业务场景：用户注册登录、商品浏览与检索、购物车管理、订单全流程（创建 → 支付 → 发货 → 收货 → 评价）、收货地址管理、商品收藏等。

主要设计特点：

- **分层架构**：Controller → Service → Mapper 标准三层结构，职责清晰。
- **JWT 双 Token 无感刷新**：access token 短时效（1 小时）+ refresh token 长时效（7 天），access token 过期后前端可用 refresh token 静默换新，refresh token 滚动续期，提升用户体验与安全性。
- **统一响应与全局异常处理**：所有接口返回统一 `Response<T>` 结构，业务异常通过 `BusinessException` 抛出并由全局处理器捕获。
- **Spring Security + BCrypt**：基于过滤器链的 JWT 认证授权，密码采用 BCrypt 加盐存储。
- **MyBatis Plus 分页**：内置分页插件，支持商品列表分页查询。
- **Swagger / OpenAPI 文档**：集成 springdoc，自动生成在线 API 文档。

---

## 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 语言 | Java | 1.8 |
| 框架 | Spring Boot | 2.7.18 |
| 安全 | Spring Security | 随 Boot 版本 |
| ORM | MyBatis Plus | 3.5.5 |
| 数据库 | MySQL | 5.7+ / 8.x |
| 认证 | JJWT | 0.11.5 |
| API 文档 | springdoc-openapi-ui | 1.6.15 |
| 工具库 | Lombok、Apache Commons Lang3 | 3.18.0 |
| 构建 | Maven | 3.x（内置 Wrapper） |

---

## 项目结构

```
pure-mall-backend/
├── mvnw / mvnw.cmd                 # Maven Wrapper
├── pom.xml                         # Maven 依赖与构建配置
├── mall_database.sql               # 数据库初始化脚本（建表 + 示例数据）
├── backend_task_list-trae.md       # 开发任务清单
├── scripts/                        # 一键启动脚本
│   ├── start-all.bat / .sh         # 一键启动（初始化数据库 + 启动后端）
│   ├── init-database.bat / .sh     # 数据库初始化（建库 + 导入 SQL）
│   └── start-backend.bat / .sh     # 构建并运行后端 jar
└── src/main/
    ├── java/com/puremall/
    │   ├── PureMallApplication.java     # Spring Boot 启动入口（@MapperScan）
    │   ├── config/                      # 配置类
    │   │   ├── CorsConfig.java          #   跨域配置
    │   │   ├── JwtConfig.java           #   JWT 配置（密钥 / 过期时间）
    │   │   ├── MyBatisConfig.java       #   MyBatis Plus 分页插件
    │   │   ├── SecurityConfig.java      #   Spring Security 过滤器链
    │   │   └── SwaggerConfig.java       #   OpenAPI 文档配置
    │   ├── controller/                  # 控制层（11 个控制器 / 54 个接口）
    │   ├── service/                     # 服务接口
    │   │   └── impl/                    # 服务实现
    │   ├── mapper/                      # 数据访问层（注解式 SQL）
    │   ├── entity/                      # 实体类（10 个）
    │   ├── security/
    │   │   └── JwtAuthenticationFilter.java   # JWT 认证过滤器
    │   ├── utils/                       # 工具类
    │   │   ├── JwtUtils.java            #   JWT 生成 / 解析 / 校验
    │   │   ├── PasswordUtils.java       #   BCrypt 密码加解密
    │   │   └── OrderNumberUtils.java    #   订单号生成
    │   ├── exception/                   # 异常处理
    │   │   ├── BusinessException.java   #   业务异常
    │   │   └── GlobalExceptionHandler.java  # 全局异常处理器
    │   └── response/
    │       └── Response.java            # 统一响应封装
    └── resources/
        └── application.properties       # 应用配置
```

---

## 功能模块

| 模块 | 说明 |
|------|------|
| 用户管理 | 注册、登录、登出、查询/更新用户信息、修改密码、上传头像、用户名/邮箱/手机号可用性校验 |
| 鉴权 | JWT access token 签发、refresh token 滚动续期无感刷新 |
| 商品管理 | 商品分页查询、商品详情、按分类查询、全部商品列表 |
| 商品图片 | 查询商品图片列表 |
| 商品规格 | 查询商品规格列表 |
| 商品评价 | 查询商品评价列表 |
| 购物车 | 增删改查、选中/全选切换、数量修改、统计信息、清空 |
| 收货地址 | 增删改查、默认地址设置、地址数量统计 |
| 收藏夹 | 添加/移除收藏、收藏列表、收藏状态检查 |
| 订单管理 | 创建、查询、详情、支付、确认收货、物流、备注、评价、删除 |
| 订单商品 | 订单商品的批量添加、查询、删除 |

---

## 数据库设计

数据库名：`pure_mall`，字符集 `utf8mb4`。共 10 张表，字段命名采用驼峰（`map-underscore-to-camel-case=false`）。

| 序号 | 表名 | 说明 | 关键字段 |
|------|------|------|----------|
| 1 | `users` | 用户表 | id、username、password（BCrypt）、email、phone、sex、avatar、status |
| 2 | `products` | 商品表 | id、name、brief、price、originalPrice、sales、stock、categoryLabel、status |
| 3 | `product_images` | 商品图片表 | productId、imageUrl、sortOrder |
| 4 | `product_specs` | 商品规格表 | productId、name、price、stock、color、size |
| 5 | `product_reviews` | 商品评价表 | productId、userId、rating（1-5）、content |
| 6 | `addresses` | 收货地址表 | userId、name、phone、province/city/district/street、detail、isDefault |
| 7 | `wishlist_items` | 收藏夹商品项表 | userId、productId（联合唯一） |
| 8 | `cart_items` | 购物车项表 | userId、productId、spec、name、imageUrl、quantity、selected、price |
| 9 | `orders` | 订单表 | orderNumber、userId、orderAmount、status、paymentMethod、收货人信息、各时间节点 |
| 10 | `order_items` | 订单商品项表 | orderNumber、name、imageUrl、userId、quantity、price |

**订单状态流转**：`pending`（待支付）→ `paid`（已支付）→ `shipped`（已发货）→ `completed`（已完成）；任意待支付状态可转为 `cancelled`（已取消）。

**表关系**：商品相关表（图片/规格/评价）通过 `productId` 外键关联 `products`；用户业务表（地址/收藏/购物车/订单）通过 `userId` 外键关联 `users`，均设置 `ON DELETE CASCADE` 级联删除。

---

## 快速开始

### 环境要求

- **JDK** 1.8 及以上
- **MySQL** 5.7+ / 8.x（默认服务名 `MySQL`，监听 `localhost:3306`）
- **Maven** 3.x（项目已内置 `mvnw` Wrapper，可不单独安装）
- Windows 环境需可执行 `.bat` 脚本；Linux/macOS 使用 `.sh` 脚本

### 方式一：一键启动（推荐）

一键脚本会自动完成：检测/启动 MySQL 服务 → 创建数据库并导入 `mall_database.sql` → 构建并启动后端。

**Windows：**

```bash
# 直接双击，或在终端运行（启动 MySQL 服务可能弹出 UAC 提权，请点击"是"）
scripts\start-all.bat
```

**Linux / macOS：**

```bash
bash scripts/start-all.sh
```

启动成功后：

- 后端服务地址：<http://localhost:8080>
- Swagger UI：<http://localhost:8080/swagger-ui/index.html>

### 方式二：分步启动

1. **初始化数据库**

   ```bash
   # Windows
   scripts\init-database.bat
   # Linux / macOS
   bash scripts/init-database.sh
   ```

2. **构建并启动后端**

   ```bash
   # Windows
   scripts\start-backend.bat
   # Linux / macOS
   bash scripts/start-backend.sh
   ```

### 方式三：手动启动

```bash
# 1. 导入数据库
mysql -u root -p --default-character-set=utf8mb4 < mall_database.sql

# 2. 打包（跳过测试）
./mvnw package -DskipTests

# 3. 运行
java -jar target/pure-mall-backend-0.0.1-SNAPSHOT.jar
```

也可在 IDE 中直接运行 `PureMallApplication.main()`。

---

## 配置说明

核心配置位于 `src/main/resources/application.properties`：

```properties
# 数据源（密码通过环境变量 DB_PASSWORD 注入，本地值在 application-local.properties 中）
spring.datasource.url=jdbc:mysql://localhost:3306/pure_mall?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# MyBatis Plus
mybatis-plus.configuration.map-underscore-to-camel-case=false
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
mybatis-plus.mapper-locations=classpath:mapper/*.xml
mybatis-plus.type-aliases-package=com.puremall.entity

# JWT 双 Token（密钥通过环境变量 JWT_SECRET 注入，本地值在 application-local.properties 中）
jwt.secret=${JWT_SECRET}
jwt.expiration=3600000            # access token 有效期：1 小时
jwt.refresh-expiration=604800000  # refresh token 有效期：7 天

# 激活本地配置覆盖（application-local.properties 存放真实密钥，已被 .gitignore 忽略）
spring.profiles.active=local
```

> **修改数据库连接 / 密钥**：数据库密码与 JWT 密钥已从版本控制中移除，改为存放在本地配置文件中：
> - Spring 应用：`src/main/resources/application-local.properties`（从 `application-local.properties.example` 复制，已被 `.gitignore` 忽略）
> - 启动脚本：`scripts/.env`（从 `.env.example` 复制，已被 `.gitignore` 忽略）
> - 生产环境：通过环境变量 `DB_PASSWORD` / `JWT_SECRET` 注入。
>
> **服务端口**：默认 `8080`（Spring Boot 默认值，未显式配置）。

---

## API 接口概览

共 **11** 个控制器、**54** 个接口，统一前缀 `/api`。除 `/api/**` 全部放行外，其余请求需携带有效 JWT（见 [认证机制](#认证机制jwt-双-token)）。

> 所有接口统一返回 `Response<T>`：`{ "code": 200, "message": "success", "data": ... }`。

### 1. 用户管理 — `/api/user`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/user/getUserId` | 根据用户名获取用户 ID |
| POST | `/api/user/register` | 用户注册 |
| POST | `/api/user/login` | 用户登录（返回 user + access token + refresh token） |
| GET | `/api/user/getInfo` | 获取用户信息 |
| PUT | `/api/user/updateInfo` | 更新用户信息 |
| POST | `/api/user/logout` | 用户登出 |
| PUT | `/api/user/password` | 修改密码 |
| POST | `/api/user/avatar` | 上传用户头像 |
| POST | `/api/user/check-username` | 检查用户名是否可用 |
| POST | `/api/user/check-email` | 检查邮箱是否可用 |
| POST | `/api/user/check-phone` | 检查手机号是否可用 |

### 2. 鉴权 — `/api/auth`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/refresh` | 刷新 access token（refresh token 滚动续期，失败返回 401） |

### 3. 商品管理 — `/api/product`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/product/page` | 分页获取商品列表（`pageNum`、`pageSize`、`categoryLabel`） |
| GET | `/api/product/{productId}` | 获取商品详情 |
| GET | `/api/product/category/{categoryLabel}` | 按分类获取商品（可分页） |
| GET | `/api/product/productList` | 获取所有商品列表 |

### 4. 商品图片 — `/api/product/image`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/product/image/product/{productId}` | 获取商品图片列表 |

### 5. 商品规格 — `/api/product/spec`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/product/spec/product/{productId}` | 获取商品规格列表 |

### 6. 商品评价 — `/api/product/review`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/product/review/product/{productId}` | 获取商品评价列表 |

### 7. 购物车 — `/api/cart`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/cart/getCartItems` | 获取购物车及商品列表（含统计） |
| POST | `/api/cart/addCartItems` | 添加商品到购物车 |
| DELETE | `/api/cart/deleteCartItem` | 删除购物车项 |
| DELETE | `/api/cart/deleteSelectedCartItems` | 删除选中的购物车商品 |
| DELETE | `/api/cart/clearCartItems` | 清空购物车 |
| PUT | `/api/cart/updateCartItemQuantity` | 修改购物车项数量 |
| PUT | `/api/cart/selectedCartItem` | 切换单个购物车项选中状态 |
| PUT | `/api/cart/selectedAll` | 切换所有购物车项选中状态 |
| GET | `/api/cart/statistics` | 获取购物车统计信息 |
| GET | `/api/cart/selected` | 获取选中的购物车商品 |

### 8. 收货地址 — `/api/address`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/address/userAddressList` | 获取用户地址列表 |
| GET | `/api/address/default` | 获取默认地址 |
| GET | `/api/address/{addressId}` | 根据 ID 获取地址 |
| POST | `/api/address/addAddress` | 添加地址 |
| PUT | `/api/address/updateAddress/{addressId}` | 更新地址 |
| DELETE | `/api/address/deleteAddress/{addressId}` | 删除地址 |
| PUT | `/api/address/{addressId}/default` | 设置默认地址 |
| GET | `/api/address/count` | 获取地址数量 |

### 9. 收藏夹 — `/api/wishlist`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/wishlist/getWishlistItems` | 获取收藏夹商品列表 |
| POST | `/api/wishlist/addWishlistItem` | 添加商品到收藏夹 |
| DELETE | `/api/wishlist/removeWishlistItem` | 从收藏夹移除商品 |
| GET | `/api/wishlist/checkInWishlistItem` | 检查商品是否已收藏 |

### 10. 订单管理 — `/api/order`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/order/userOrders` | 获取用户订单列表 |
| POST | `/api/order/addOrder` | 创建订单 |
| DELETE | `/api/order/deleteOrder` | 删除订单 |
| GET | `/api/order/{orderNumber}` | 获取订单详情（含商品、物流） |
| PUT | `/api/order/{orderNumber}/pay` | 支付订单 |
| PUT | `/api/order/{orderNumber}/confirm` | 确认收货 |
| GET | `/api/order/{orderNumber}/items` | 获取订单商品列表 |
| GET | `/api/order/{orderNumber}/logistics` | 获取订单物流信息 |
| POST | `/api/order/{orderNumber}/remark` | 添加订单备注 |
| POST | `/api/order/{orderNumber}/review` | 评价订单商品 |

### 11. 订单商品 — `/api/orderItem`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/orderItem/addOrderItem` | 批量添加订单商品 |
| GET | `/api/orderItem/getOrderItem` | 查询订单商品 |
| DELETE | `/api/orderItem/deleteOrderItem` | 删除订单商品 |

> 完整的请求/响应字段说明可启动后访问 Swagger UI 查看：<http://localhost:8080/swagger-ui/index.html>

---

## 认证机制（JWT 双 Token）

系统采用 **access token + refresh token** 双 Token 架构，实现前端无感刷新：

1. **登录签发**（`POST /api/user/login`）：校验账号密码成功后，签发：
   - **access token**：有效期 1 小时，claim 含 `userId`、`username`、`type=access`，前端放入 `Authorization: Bearer <token>` 请求头。
   - **refresh token**：有效期 7 天，`type=refresh`，仅用于续期，不可作为 access token 使用。

2. **请求鉴权**（`JwtAuthenticationFilter`）：
   - 放行 OPTIONS 预检请求；
   - 从 `Authorization` 头解析 token，校验有效后写入 `SecurityContext`；
   - token 过期/无效时返回 `401`（`{"code":401,"message":"登录已过期，请重新登录"}`），并手动补齐 CORS 响应头，前端据此触发刷新流程。

3. **无感刷新**（`POST /api/auth/refresh`）：前端用 refresh token 调用刷新接口，校验 token 类型为 `refresh` 且有效后，签发**新的 access token 与新的 refresh token**（滚动续期），前端替换本地 token 继续原请求；refresh token 也过期则返回 `401`，前端强制登出。

4. **安全配置**（`SecurityConfig`）：`/api/**` 与 Swagger 相关路径放行，其余请求需认证；CSRF 关闭，启用 CORS；JWT 过滤器注册于 `UsernamePasswordAuthenticationFilter` 之前。

5. **密码安全**：注册与修改密码时使用 `BCryptPasswordEncoder` 加盐哈希存储，登录时通过 `matchesPassword` 校验。

---

## 统一响应与异常处理

**统一响应体** `Response<T>`：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

- `Response.success(data)` → `code=200, message="success"`
- `Response.fail(code, message)` / `Response.fail(message)` → 失败响应（默认 code=500）

**全局异常处理** `GlobalExceptionHandler`：

- `BusinessException`：业务异常，返回其携带的 `code` 与 `message`（如"用户名已存在"、"密码错误"、"订单状态错误，无法支付"等）。
- `Exception`：其他未捕获异常，打印堆栈并返回 `code=500, message="服务器内部错误"`。

业务层通过抛出 `BusinessException` 表达业务规则失败，控制器无需手动 try-catch，保证响应结构统一。

---

## 默认账号

数据库初始化脚本 `mall_database.sql` 内置示例数据与账号：

| 用户名 | 密码 | 说明 |
|--------|------|------|
| `admin` | `admin123` | 默认管理员账号 |
| `user` | `user123` | 示例普通用户 |

> 示例数据还包含 48 件商品（覆盖 T 恤、衬衫、卫衣、毛衣、外套、牛仔裤、休闲裤、裙子、鞋子、配饰、内衣、箱包等分类）、商品图片、规格、评价、收货地址、购物车、收藏夹及订单数据，便于直接联调测试。

---

## 许可证

本项目暂未指定开源许可证，仅供学习与内部使用。
