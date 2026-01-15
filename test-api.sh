#!/bin/bash

# 测试脚本：验证后端API在HTTPS下的可访问性

echo "=== 测试后端API在HTTPS下的可访问性 ==="
echo ""

# 1. 测试本地HTTPS访问
echo "1. 测试本地HTTPS访问 (https://192.168.137.128/api/product/productList)"
curl -k -s -w "\nHTTP状态码: %{http_code}\n响应时间: %{time_total}s\n" "https://192.168.137.128/api/product/productList"
echo ""

# 2. 测试HTTP访问（应该重定向到HTTPS）
echo "2. 测试HTTP访问 (http://192.168.137.128:8081/api/product/productList)"
curl -k -s -w "\nHTTP状态码: %{http_code}\n响应时间: %{time_total}s\n" -L "http://192.168.137.128:8081/api/product/productList"
echo ""

# 3. 测试Nginx直接访问后端
echo "3. 测试Nginx直接访问后端 (http://backend:8080/api/product/productList)"
docker exec -i pure-mall-nginx curl -s -w "\nHTTP状态码: %{http_code}\n响应时间: %{time_total}s\n" "http://backend:8080/api/product/productList"
echo ""

# 4. 检查SSL证书信息
echo "4. 检查SSL证书信息"
echo "命令: openssl s_client -showcerts -connect 192.168.137.128:443 < /dev/null"
echo "如果无法执行，手动在虚拟机中运行以上命令查看证书信息"
echo ""

echo "=== 测试完成 ==="
echo ""
echo "请检查输出结果："
echo "- 第1项应该返回200状态码和API数据"
echo "- 第2项应该先返回301重定向，然后返回200状态码和API数据"
echo "- 第3项应该返回200状态码和API数据（在容器内部测试）"
echo "- 第4项应该显示SSL证书的详细信息，包括CN字段为192.168.137.128"