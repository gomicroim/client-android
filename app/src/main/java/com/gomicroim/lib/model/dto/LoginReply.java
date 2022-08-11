package com.gomicroim.lib.model.dto;

public class LoginReply {
    public String accessToken;
    public int accessExpires;
    public String refreshToken;
    public int refreshExpires;

    @Override
    public String toString() {
        return "LoginInfo{" +
                "accessToken='" + accessToken + '\'' +
                ", accessExpires=" + accessExpires +
                ", refreshToken='" + refreshToken + '\'' +
                ", refreshExpires=" + refreshExpires +
                '}';
    }
}
