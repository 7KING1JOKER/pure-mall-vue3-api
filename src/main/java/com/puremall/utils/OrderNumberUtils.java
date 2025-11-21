package com.puremall.utils;

/**
 * 订单号生成工具类
 * 用于生成唯一的订单编号
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderNumberUtils {

    // 生成唯一订单号
    public static String generateOrderNumber() {
        // 格式：yyyyMMddHHmmss + 6位随机数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateStr = sdf.format(new Date());
        
        // 生成6位随机数
        Random random = new Random();
        int randomNum = 100000 + random.nextInt(900000); // 6位随机数
        
        return dateStr + randomNum;
    }
}