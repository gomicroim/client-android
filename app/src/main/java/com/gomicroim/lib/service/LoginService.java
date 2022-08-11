package com.gomicroim.lib.service;

import com.gomicroim.lib.Observer;
import com.gomicroim.lib.model.constant.StatusCode;
import com.gomicroim.lib.model.dto.DeviceReply;
import com.gomicroim.lib.model.dto.DeviceReq;
import com.gomicroim.lib.transport.InvocationFuture;
import com.gomicroim.lib.model.dto.LoginReply;

/**
 * 登录业务
 */
public interface LoginService {
    /**
     * 设备注册
     */
    InvocationFuture<DeviceReply> deviceRegister(DeviceReq devInfo);

    /**
     * 登录认证
     *
     * @param phone      手机号
     * @param code       验证码
     * @param appVersion 客户端版本
     * @return 结果回调
     */
    InvocationFuture<LoginReply> login(String phone, String code, String appVersion);

    /**
     * 连接观察器
     *
     * @param observer 观察器
     * @param register true: 注册, false: 注销
     */
    void observeOnlineStatus(Observer<StatusCode> observer, boolean register);
}
