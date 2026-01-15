# Pure Mall 后端部署流程文档（Docker版）

## 1. 部署前准备

### 1.1 项目概述
- 项目类型：Spring Boot 2.7.18 后端应用
- 技术栈：Java 8, MyBatis Plus, MySQL, JWT, Spring Security
- 打包方式：Maven JAR + Docker容器
- 前端已部署：GitHub Pages (https://your-username.github.io/pure-mall-vue3/)
- 后端项目路径：`e:\vue-project\pure-mall-api\pure-mall-backend`
- MySQL数据库脚本：`e:\vue-project\pure-mall-api\pure-mall-backend\mall_database.sql`

### 1.2 部署环境说明

本次部署使用以下环境：
- **虚拟化软件**：VMware Workstation
- **虚拟机操作系统**：Ubuntu 20.04 LTS
- **部署方式**：Docker容器化部署
- **前端状态**：已通过GitHub Actions部署到GitHub Pages

#### 虚拟机配置建议：
| 配置项 | 建议值 |
|-------|-------|
| CPU | 2核或以上 |
| 内存 | 6GB或以上（分配给虚拟机4GB+） |
| 存储 | 30GB+虚拟磁盘（推荐SSD） |
| 网络 | 桥接模式（便于公网访问）或NAT模式（仅内部访问） |


#### 1.3.6 虚拟机性能优化建议

为了确保Docker容器和应用程序能够流畅运行，建议对虚拟机进行以下性能优化：

1. **分配足够的资源**：
   - CPU：至少2核，4核更佳
   - 内存：至少6GB（分配给虚拟机4GB+）
   - 存储：至少30GB虚拟磁盘（推荐使用SSD或设置为"预分配全部磁盘空间"以提高性能）

2. **虚拟机设置优化**：
   - 在VMware Workstation中，点击"编辑虚拟机设置" -> "硬件" -> "处理器"：
     - 启用"虚拟化Intel VT-x/EPT或AMD-V/RVI"（如果可用）
   - 在"硬件" -> "内存"：
     - 取消勾选"启用内存页面共享"

3. **Ubuntu系统优化**：
   - 关闭不必要的系统服务：

```bash
# 查看并停止不必要的服务
systemctl list-unit-files --type=service
# 例如，停止蓝牙服务（如果不需要）
sudo systemctl stop bluetooth
sudo systemctl disable bluetooth
```

## 2. Docker环境搭建

### 2.1 登录Ubuntu虚拟机

可以通过两种方式登录虚拟机：
- **直接登录**：在VMware Workstation窗口中直接输入用户名和密码
- **SSH远程登录**：使用SSH工具（如Xshell、Putty、Windows Terminal）登录

```bash
# SSH登录命令
ssh your-username@your-vm-ip-address
```

### 2.2 安装Docker

在Ubuntu 20.04虚拟机中执行以下命令安装Docker：

```bash
# 更新包列表
sudo apt update

# 安装必要的依赖包
sudo apt install -y apt-transport-https ca-certificates curl gnupg-agent software-properties-common

# 添加Docker的官方GPG密钥
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

# 添加Docker的稳定版仓库
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"

# 更新包列表并安装Docker
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io

# 启动Docker服务并设置开机自启
sudo systemctl start docker
sudo systemctl enable docker

# 验证Docker安装
sudo docker --version

# 将当前用户添加到docker组（避免每次使用sudo）
sudo usermod -aG docker $USER
```

**注意**：添加用户到docker组后，需要重新登录或执行`newgrp docker`命令使更改生效。

### 2.3 安装Docker Compose

```bash
# 下载Docker Compose二进制文件
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.3/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# 赋予执行权限
sudo chmod +x /usr/local/bin/docker-compose

# 创建软链接
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

# 验证Docker Compose安装
docker-compose --version
```

### 2.4 Docker性能优化

为了确保Docker容器能够高效运行，建议进行以下优化配置：

```bash
# 创建或编辑Docker守护进程配置文件
sudo vim /etc/docker/daemon.json
```

添加以下内容：
```json
{
  "default-shm-size": "2g",
  "storage-driver": "overlay2"
}
```

然后重启Docker服务：
```bash
sudo systemctl restart docker
```

## 3. 项目准备

### 3.1 传输项目文件到虚拟机

由于项目文件在Windows主机上，需要将其传输到Ubuntu虚拟机中。可以使用以下方法：

#### 方法1：使用SCP命令传输

在Windows主机的命令提示符或PowerShell中执行：

```bash
# 传输整个后端项目目录
scp -r "e:\vue-project\pure-mall-api\pure-mall-backend" your-username@your-vm-ip-address:/home/your-username/

scp -r "e:\vue-project\pure-mall-api\pure-mall-backend" niko@192.168.137.128:/home/niko/
```

#### 方法2：使用共享文件夹

1. 在VMware Workstation中，选中虚拟机，点击"编辑虚拟机设置"
2. 点击"选项"标签页，选择"共享文件夹"
3. 选择"总是启用"，点击"添加..."按钮
4. 浏览并选择Windows主机上的项目目录`e:\vue-project\pure-mall-api\pure-mall-backend`
5. 设置共享名称，如`pure_mall_backend`，点击"完成"
6. 在Ubuntu虚拟机中，安装VMware Tools（如果未安装）：

```bash
sudo apt install -y open-vm-tools open-vm-tools-desktop
```

7. 挂载共享文件夹：

```bash
# 创建挂载点
mkdir -p ~/pure-mall-backend

# 挂载共享文件夹
sudo mount -t vmhgfs .host:/pure_mall_backend ~/pure-mall-backend
```

### 3.2 进入项目目录

```bash
cd ~/pure-mall-backend
```

### 3.3 检查项目文件

确保项目文件和MySQL脚本已正确传输：

```bash
# 查看项目目录结构
ls -la

# 确认MySQL脚本存在
ls -l mall_database.sql
```

### 3.4 创建Dockerfile

确保项目根目录下已有`Dockerfile`文件，内容如下：

```dockerfile
# 使用官方Java 8镜像作为基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制Maven构建文件
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .

# 赋予执行权限
RUN chmod +x mvnw

# 下载依赖
RUN ./mvnw dependency:go-offline -B

# 复制项目源代码
COPY src/ src/

# 构建项目
RUN ./mvnw clean package -DskipTests

# 复制构建后的jar文件到指定位置
RUN cp target/*.jar app.jar

# 设置环境变量
ENV SPRING_PROFILES_ACTIVE=prod

# 暴露端口
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 3.5 创建Docker Compose配置文件

确保项目根目录下已有`docker-compose.yml`文件，内容如下：

```yaml
version: '3.8'

services:
  # MySQL数据库服务
  mysql:
    image: mysql:8.0
    container_name: pure-mall-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: 123456  # 替换为您想要设置的MySQL root密码
      MYSQL_DATABASE: pure_mall  # 数据库名称，建议保持不变
      MYSQL_USER: niko  # 替换为您想要创建的数据库用户名
      MYSQL_PASSWORD: 123456  # 替换为您为数据库用户设置的密码
    volumes:
      - mysql_data:/var/lib/mysql
      - ./mall_database.sql:/docker-entrypoint-initdb.d/mall_database.sql
    ports:
      - "3306:3306"
    networks:
      - pure-mall-network

  # 后端应用服务
  backend:
    build: .
    container_name: pure-mall-backend
    restart: always
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/pure_mall?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: niko  # 与MySQL服务中的MYSQL_USER保持一致
      SPRING_DATASOURCE_PASSWORD: 123456  # 与MySQL服务中的MYSQL_PASSWORD保持一致
      JWT_SECRET: 5f2d1a8e9b7c3f4e2a6d8b9c7e3f2d1a8e9b7c3f4e2a6d8b9c7e3f2d  # 替换为您自己的JWT密钥，建议使用随机生成的长字符串
      SPRING_WEB_CORS_ALLOWED_ORIGINS: https://7king1joker.github.io  # 替换为您的GitHub Pages域名
    depends_on:
      - mysql
    ports:
      - "8080:8080"
    networks:
      - pure-mall-network

  # Nginx反向代理服务
  nginx:
    image: nginx:alpine
    container_name: pure-mall-nginx
    restart: always
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
      - ./ssl:/etc/nginx/ssl  # SSL证书目录（可选）
    depends_on:
      - backend
    networks:
      - pure-mall-network

# 数据卷定义
volumes:
  mysql_data:

# 网络定义
networks:
  pure-mall-network:
    driver: bridge
```

### 3.6 创建Nginx配置文件

确保项目根目录下已有`nginx.conf`文件，内容如下：

```nginx
events {
    worker_connections 1024;
}

http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    server {
        listen 80;
        server_name 192.168.137.128;  # 替换为您的虚拟机实际IP地址

        location / {
            proxy_pass http://backend:8080;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }

        # 静态资源缓存配置
        location ~* \.(jpg|jpeg|png|gif|ico|css|js|pdf)$ {
            proxy_pass http://backend:8080;
            expires 30d;
            add_header Cache-Control "public, no-transform";
        }
    }
}
```

### 3.7 创建生产环境配置文件

确保`src/main/resources`目录下已有`application-prod.properties`文件，内容如下：

```properties
# 应用名称
spring.application.name=pure-mall-backend

# 数据源配置（Docker环境下会被Docker Compose环境变量覆盖）
spring.datasource.url=jdbc:mysql://localhost:3306/pure_mall?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  
# MyBatis Plus配置
mybatis-plus.configuration.map-underscore-to-camel-case=false
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
mybatis-plus.mapper-locations=classpath:mapper/*.xml
mybatis-plus.type-aliases-package=com.puremall.entity
  
# JWT配置
jwt.secret=your_secure_jwt_secret_key_here  # 替换为您自己的JWT密钥，与docker-compose.yml中的设置保持一致
jwt.expiration=86400000

# 服务器端口配置
server.port=8080

# CORS配置（Docker环境下会被Docker Compose环境变量覆盖）
spring.web.cors.allowed-origins=https://your-username.github.io  # 替换为您的GitHub Pages域名
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
```

## 4. Docker部署执行

### 4.1 启动所有服务

在项目根目录执行以下命令：

```bash
docker-compose up -d
```

### 4.2 查看容器状态

```bash
docker-compose ps
```

### 4.3 查看应用日志

```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f backend
```

### 4.4 验证数据库初始化

```bash
# 查看MySQL容器日志，确认数据库脚本执行成功
docker-compose logs -f mysql
```

## 5. 前端API地址配置

由于您的前端已通过GitHub Actions部署到GitHub Pages，需要更新前端的API请求地址指向虚拟机中部署的后端服务。

### 5.1 更新前端API地址

在前端项目的GitHub仓库中，找到并编辑`.env.production`文件：

```properties
# 生产环境配置
# 后端API基础地址，指向虚拟机中部署的后端服务
# 如果虚拟机使用桥接模式，使用虚拟机的IP地址
# 如果虚拟机使用NAT模式，使用主机的IP地址 + VMware NAT转发端口
# 注意：如果前端部署在HTTPS环境（如GitHub Pages），后端API也需要支持HTTPS，否则会出现混合内容错误
# 解决方案1：为后端配置HTTPS（推荐）
# 解决方案2：使用第三方HTTPS代理服务
VITE_APP_BASE_API=http://your-vm-ip-address:8080
```

- 如果是**桥接模式**：直接使用虚拟机的IP地址，如`http://192.168.1.101`
- 如果是**NAT模式**：需要在VMware中配置端口转发，然后使用`http://主机IP:转发端口`，如`http://192.168.0.100:8080`

#### NAT模式端口转发配置步骤

如果您的虚拟机使用NAT模式，需要按照以下步骤配置端口转发：

1. 打开VMware Workstation
2. 点击顶部菜单栏的"编辑" -> "虚拟网络编辑器"
3. 选中VMnet8（默认对应NAT模式）
4. 点击"NAT设置"按钮
5. 在"端口转发"选项卡中，点击"添加"按钮
6. 填写端口转发规则：
   - 名称：可自定义（如pure-mall-https）
   - 类型：选择TCP
   - 主机IP：留空（表示使用主机的所有IP地址）
   - 主机端口：选择一个未被占用的端口（推荐使用443，即标准HTTPS端口）
   - 虚拟机IP：填写虚拟机的IP地址（可通过`ip addr show`命令查看，例如192.168.137.128）
   - 虚拟机端口：填写Nginx服务的HTTPS端口（必须为443，对应docker-compose.yml中nginx服务的443:443映射）
   - 描述：可自定义（如Pure Mall HTTPS服务端口转发）
7. 点击"确定"保存规则
8. 确保虚拟机中的后端服务已启动

配置完成后，前端API地址应设置为：`https://主机IP:主机端口`（如果使用443端口，可省略端口号），例如：
```properties
VITE_APP_BASE_API=https://192.168.0.100
```

其中，`主机IP`是您物理机的IP地址，`主机端口`是您在端口转发规则中设置的主机端口（如果使用443端口可省略）。

**重要说明：**
- 由于前端部署在GitHub Pages（HTTPS环境），后端API必须通过HTTPS访问，否则会出现混合内容错误
- 必须转发到Nginx的443端口（HTTPS），而不是直接转发到后端的8080端口（HTTP）
- 如果使用非标准端口（如4433），前端API地址需要包含端口号，例如：`https://192.168.0.100:4433`
