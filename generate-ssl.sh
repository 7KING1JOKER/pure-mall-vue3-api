#!/bin/bash

# 创建SSL证书目录
mkdir -p ssl

# 生成自签名SSL证书
echo "正在生成自签名SSL证书..."
openssl req -x509 -nodes -days 3650 -newkey rsa:2048 \
    -keyout ssl/key.pem \
    -out ssl/cert.pem \
    -subj "/CN=192.168.137.128/O=PureMall/C=CN"

# 设置证书权限
chmod 600 ssl/key.pem
chmod 644 ssl/cert.pem

echo "SSL证书生成完成！"
echo "证书位置："
echo "- 私钥：ssl/key.pem"
echo "- 证书：ssl/cert.pem"
