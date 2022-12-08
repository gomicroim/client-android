package com.gomicroim.lib.service;

import com.gomicroim.lib.Observer;
import com.gomicroim.lib.model.constant.StatusCode;
import com.gomicroim.lib.protos.user.User;
import com.gomicroim.lib.transport.InvocationFuture;
import com.gomicroim.lib.model.dto.LoginReply;

import java.io.IOException;

/**
 * 登录业务
 */
public interface LoginService {
    /**
     * 设备注册
     */
    InvocationFuture<User.RegisterReply> deviceRegister(User.RegisterRequest devInfo);

    /**
     * 登录认证
     *
     * @param phone      手机号
     * @param code       验证码
     * @param appVersion 客户端版本
     * @return 结果回调
     */
    InvocationFuture<User.AuthReply> login(String phone, String code, String appVersion);

    /**
     * 自动登录，如果本地保存了Token，需要调用
     *
     * @param tokenInfo: 本地保存的token
     * @return 如果token过期，会返回新的token，需要替换本地保存的
     */
    InvocationFuture<User.AuthReply> autoLogin(User.TokenInfo tokenInfo);

    /**
     * 刷新token
     *
     * @param refreshToken: 用于获取新的accessToken，如果也过期，请重新登录
     * @return 结果回调
     */
    InvocationFuture<User.RefreshTokenReply> refreshToken(String refreshToken);

    /**
     * 连接观察器
     *
     * @param observer 观察器
     * @param register true: 注册, false: 注销
     */
    void observeOnlineStatus(Observer<StatusCode> observer, boolean register);
}
