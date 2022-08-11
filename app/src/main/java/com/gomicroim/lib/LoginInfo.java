package com.gomicroim.lib;

/**
 * 登录信息
 */
public class LoginInfo {
    private String token;

    public LoginInfo(String token) {
        this.token = token;
    }

    /**
     * 获取jwt token
     *
     * @return token
     */
    public String getToken() {
        return token;
    }
}
