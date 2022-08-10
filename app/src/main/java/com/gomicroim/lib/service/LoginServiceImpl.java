package com.gomicroim.lib.service;

import com.gomicroim.lib.helper.HttpResponseCallBack;
import com.gomicroim.lib.helper.OkHttpUtils;
import com.gomicroim.lib.model.dto.DeviceReply;
import com.gomicroim.lib.model.dto.DeviceReq;
import com.gomicroim.lib.model.dto.LoginInfo;
import com.gomicroim.lib.transport.AbortableFuture;
import com.gomicroim.lib.transport.InvocationFuture;
import com.gomicroim.lib.transport.InvocationFutureImpl;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LoginServiceImpl implements LoginService {
    private static final String URL_DEVICE_REGISTER = "/auth/device/register";

    @Override
    public InvocationFuture<DeviceReply> deviceRegister(DeviceReq devInfo) {
        InvocationFutureImpl<DeviceReply> cb = new InvocationFutureImpl<>();

        OkHttpUtils.postAsyncJson(URL_DEVICE_REGISTER, null, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String json) throws JsonSyntaxException {
                DeviceReply result = new Gson().fromJson(json, DeviceReply.class);
                if (cb.getCallback() != null) {
                    cb.getCallback().onSuccess(result);
                }
            }

            @Override
            public void onException(Throwable exception) {
                if (cb.getCallback() != null) {
                    cb.getCallback().onException(exception);
                }
            }
        });

        return cb;
    }

    @Override
    public AbortableFuture<LoginInfo> login(String phone, String code) {
        return null;
    }
}
