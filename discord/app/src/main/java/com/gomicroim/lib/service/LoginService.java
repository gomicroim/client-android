package com.gomicroim.lib.service;

import com.gomicroim.lib.transport.AbortableFuture;
import com.gomicroim.lib.model.Device;
import com.gomicroim.lib.transport.RequestCallback;
import com.gomicroim.lib.model.LoginInfo;

/**
 * 登录业务
 */
public interface LoginService {
    /**
     * 设备注册
     */
    RequestCallback<Device> deviceRegister(Device devInfo);

    /**
     * 登录认证
     *
     * @param phone：手机号
     * @param code：验证码
     * @return 结果回调
     */
    AbortableFuture<LoginInfo> login(String phone, String code);
}
