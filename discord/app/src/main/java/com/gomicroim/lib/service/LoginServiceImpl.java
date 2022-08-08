package com.gomicroim.lib.service;

import com.gomicroim.lib.model.Device;
import com.gomicroim.lib.model.LoginInfo;
import com.gomicroim.lib.transport.AbortableFuture;
import com.gomicroim.lib.transport.RequestCallback;

public class LoginServiceImpl implements LoginService {

    @Override
    public RequestCallback<Device> deviceRegister(Device devInfo) {
        return null;
    }

    @Override
    public AbortableFuture<LoginInfo> login(String phone, String code) {
        return null;
    }
}
