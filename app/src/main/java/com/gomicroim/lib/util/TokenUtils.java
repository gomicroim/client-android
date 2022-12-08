package com.gomicroim.lib.util;

/**
 * Token 相关
 */
public class TokenUtils {
    /**
     * 检测token是否已经过期
     *
     * @param atExpiresAt: 过期时间戳
     * @return 结果
     */
    public static boolean isTokenExpires(long atExpiresAt) {
        long curTimeStamp = System.currentTimeMillis() / 1000;
        return curTimeStamp > atExpiresAt;
    }
}
