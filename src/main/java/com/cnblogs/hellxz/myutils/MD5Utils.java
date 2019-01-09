package com.cnblogs.hellxz.myutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * <b>类名</b>: MD5Utils
 * <p><b>描    述</b> MD5加密工具类 </p>
 *
 * <p><b>创建日期</b>: 2018/8/29 17:40 </p>
 * @author  HELLXZ 张
 * @version  1.0
 * @since  jdk 1.8
 */
public class MD5Utils {

    private static final Logger log = LoggerFactory.getLogger(MD5Utils.class);

    /**
     * MD5加密
     */
    public static String getMD5Str(String str) {

        log.info("MD5加密方法，入参 str={}", str);
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("算法名或字符编码名有错误！", e);
        }

        byte[] byteArray = messageDigest.digest();
        StringBuilder buffer = new StringBuilder();

        for (byte bytes : byteArray) {
            if (Integer.toHexString(0xFF & bytes).length() == 1) {
                buffer.append("0").append(Integer.toHexString(0xFF & bytes));
            } else {
                buffer.append(Integer.toHexString(0xFF & bytes));
            }
        }

        String result = buffer.toString();
        log.info("加密结果：{}", result);
        return result;
    }

    /**
     * MD5加密后Base64转码
     */
    public static String md5AndBase64Encode(String str) {
        log.info("MD5、Base64加密方法，入参 str={}", str);
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
            String result = Base64.getEncoder().encodeToString(md5.digest());
            log.info("加密结果：{}", result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

