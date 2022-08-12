package com.gomicroim.lib.service;

import com.gomicroim.lib.Api;
import com.gomicroim.lib.Observer;
import com.gomicroim.lib.helper.HttpResponseCallBack;
import com.gomicroim.lib.helper.HttpSimpleResponse;
import com.gomicroim.lib.helper.OkHttpUtils;
import com.gomicroim.lib.model.constant.StatusCode;
import com.gomicroim.lib.model.dto.DeviceReply;
import com.gomicroim.lib.model.dto.DeviceReq;
import com.gomicroim.lib.model.dto.LoginReply;
import com.gomicroim.lib.transport.InvocationFuture;
import com.gomicroim.lib.transport.InvocationFutureImpl;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class LoginServiceImpl implements LoginService {
    private static final String URL_DEVICE_REGISTER = "/auth/device/register";
    private static final String URL_AUTH_LOGIN = "/auth/login";
    private Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Override
    public InvocationFuture<DeviceReply> deviceRegister(DeviceReq devInfo) {
        InvocationFutureImpl<DeviceReply> cb = new InvocationFutureImpl<>();

        String json = new Gson().toJson(devInfo);
        log.debug("deviceRegister, deviceInfo:{}", json);

        OkHttpUtils.postAsyncJson(URL_DEVICE_REGISTER, json,
                new HttpSimpleResponse<DeviceReply>(cb) {
                    @Override
                    public void onSuccess(String json) throws JsonSyntaxException {
                        DeviceReply result = new Gson().fromJson(json, DeviceReply.class);
                        // save token
                        OkHttpUtils.setToken(result.guestToken);
                        cb.getCallback().onSuccess(result);
                    }
                });
        return cb;
    }

    @Override
    public InvocationFuture<LoginReply> login(String phone, String code, String appVersion) {
        InvocationFutureImpl<LoginReply> cb = new InvocationFutureImpl<>();

        Map<String, String> param = new HashMap<>();
        param.put("appVersion", appVersion);
        param.put("type", "mobile");
        param.put("push_token", "");
        param.put("shumei_device_id", "");
        param.put("phone", phone);
        param.put("code", code);
        OkHttpUtils.postAsyncFormData(URL_AUTH_LOGIN, param, new HttpSimpleResponse<LoginReply>(cb) {
            @Override
            public void onSuccess(String json) throws JsonSyntaxException {
                LoginReply loginInfo = new Gson().fromJson(json, LoginReply.class);
                cb.getCallback().onSuccess(loginInfo);

                // save token again
                OkHttpUtils.setToken(loginInfo.accessToken);
                // auto connect
                Api.getWsPushService().connect(loginInfo.accessToken, Api.getOptions().gatewayAddress);
            }
        });

        return cb;
    }

    @Override
    public void observeOnlineStatus(Observer<StatusCode> observer, boolean register) {
        // Api.getWsPushService().observeOnlineStatus(observer, register);
    }
}
