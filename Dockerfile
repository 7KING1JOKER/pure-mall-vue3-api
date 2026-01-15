# 使用用户本地已有的基于Alpine的Maven 3.5.0镜像作为基础镜像（包含JDK 8）
FROM crpi-ln57l0vg937fdzxj.cn-shenzhen.personal.cr.aliyuncs.com/puremall-images/maven:3.5.0-jdk-8-alpine

# 设置工作目录
WORKDIR /app

# 安装完整的SSL相关依赖和wget工具，解决HTTPS连接问题
# 安装tzdata用于时区配置，Alpine中字符集通过环境变量设置
RUN apk add --no-cache ca-certificates openssl libressl2.5-libcrypto libressl2.5-libssl wget tzdata

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
# 设置Java默认编码为UTF-8
ENV JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8
# 设置时区为亚洲/上海
ENV TZ=Asia/Shanghai
# 为Alpine容器设置LANG环境变量，确保终端和应用使用UTF-8
ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US:en
ENV LC_ALL=en_US.UTF-8

# 暴露端口
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]