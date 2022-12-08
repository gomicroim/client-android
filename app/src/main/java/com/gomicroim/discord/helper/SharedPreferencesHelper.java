package com.gomicroim.discord.helper;

import android.content.Context;

import com.gomicroim.discord.util.SharedPreferencesUtils;
import com.gomicroim.lib.util.TokenUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class SharedPreferencesHelper {
    private SharedPreferencesUtils preferencesUtils;

    private static final String KEY_PHONE = "phone";
    private static final String KEY_PWD_CHECK = "rememberPwdChecked";
    private static final String KEY_TOKEN = "token";

    public SharedPreferencesHelper(Context context) {
        preferencesUtils = new SharedPreferencesUtils(context);
    }

    public void saveRememberPwdChecked(boolean checked) {
        preferencesUtils.putValues(new SharedPreferencesUtils.ContentValue(KEY_PWD_CHECK, checked));
    }

    public boolean readRememberPwdChecked() {
        return preferencesUtils.getBoolean(KEY_PWD_CHECK, false);
    }

    public void savePhone(String phone) {
        preferencesUtils.putValues(new SharedPreferencesUtils.ContentValue(KEY_PHONE, phone));
    }

    public String readPhone() {
        return preferencesUtils.getString(KEY_PHONE);
    }

    /**
     * 保存 token 信息，json格式。key：
     * access_token: 初始化sdk需要的token，用于访问授权
     * refresh_token: 如果token过期，需要使用refresh token换取新的token
     * at_expires: accessToken过期时间戳（秒值），较短大约一周
     * rt_expires: refreshToken过期时间戳（秒值），较长大约一个月
     * user_id: 用户ID
     */
    public void saveTokenInfo(TokenInfo tokenInfo) {
        Gson gson = new Gson();
        preferencesUtils.putValues(new SharedPreferencesUtils.ContentValue(KEY_TOKEN, gson.toJson(tokenInfo)));
    }

    /**
     * 读取 json格式的token 信息，包含过期时间
     */
    public TokenInfo readTokenInfo() {
        String json = preferencesUtils.getString(KEY_TOKEN);
        Gson gson = new Gson();
        TokenInfo tokenInfo = null;
        try {
            tokenInfo = gson.fromJson(json, TokenInfo.class);
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
        }
        return tokenInfo;
    }

    /**
     * token信息
     */
    public static class TokenInfo {
        public long userId;
        public String accessToken; // 访问token，过期时间短，通常为7天
        public long atExpires;     // accessToken 过期时间戳(s)
        public String refreshToken;// 用来刷新 accessToken，过期时间长，通常为30天，如果也过期，需要重新使用账号密码登录
        public long rtExpires;     // refreshToken 过期时间戳

        public TokenInfo(long uid, String accessToken, long atExpires, String refreshToken, long rtExpires) {
            this.userId = uid;
            this.accessToken = accessToken;
            this.atExpires = atExpires;
            this.refreshToken = refreshToken;
            this.rtExpires = rtExpires;
        }
    }
}
