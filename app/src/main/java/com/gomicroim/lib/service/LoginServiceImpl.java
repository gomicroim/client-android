package com.gomicroim.lib.service;

import com.gomicroim.lib.Api;
import com.gomicroim.lib.Observer;
import com.gomicroim.lib.helper.HttpSimpleResponse;
import com.gomicroim.lib.helper.OkHttpUtils;
import com.gomicroim.lib.model.constant.StatusCode;
import com.gomicroim.lib.model.dto.LoginReply;
import com.gomicroim.lib.protos.user.User;
import com.gomicroim.lib.transport.InvocationFuture;
import com.gomicroim.lib.transport.InvocationFutureImpl;
import com.gomicroim.lib.util.ProtoJsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServiceImpl implements LoginService {
    private static final String URL_DEVICE_REGISTER = "/auth/device/register";
    private static final String URL_AUTH_LOGIN = "/auth/login";
    private Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Override
    public InvocationFuture<User.RegisterReply> deviceRegister(User.RegisterRequest devInfo) {
        InvocationFutureImpl<User.RegisterReply> cb = new InvocationFutureImpl<>();

        String json = "";
        try {
            json = ProtoJsonUtils.toJson(devInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("deviceRegister, deviceInfo:{}", json);

        OkHttpUtils.postAsyncJson(URL_DEVICE_REGISTER, json,
                new HttpSimpleResponse<User.RegisterReply>(cb) {
                    @Override
                    public void onSuccess(String json) throws JsonSyntaxException, IOException {
                        User.RegisterReply result = (User.RegisterReply) ProtoJsonUtils.toProtoBean(User.RegisterReply.newBuilder(), json);

                        // save token
                        OkHttpUtils.setToken(result.getAccessToken());
                        cb.getCallback().onSuccess(result);
                    }
                });
        return cb;
    }

    @Override
    public InvocationFuture<User.AuthReply> login(String phone, String code, String appVersion) {
        InvocationFutureImpl<User.AuthReply> cb = new InvocationFutureImpl<>();

        User.AuthRequest req = User.AuthRequest.newBuilder().setClientType(User.AuthRequest.ClientType.clientTypeApp)
                .setLoginType(User.AuthRequest.LoginType.loginTypeMobile)
                .setByMobile(User.AuthRequest.MobileAuth.newBuilder().setPhone(phone).setCode(code))
                .build();

        String json = "";
        try {
            json = ProtoJsonUtils.toJson(req);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpUtils.postAsyncJson(URL_AUTH_LOGIN, json, new HttpSimpleResponse<User.AuthReply>(cb) {
            @Override
            public void onSuccess(String json) throws JsonSyntaxException, IOException {
                User.AuthReply reply = (User.AuthReply) ProtoJsonUtils.toProtoBean(User.AuthReply.newBuilder(), json);
                cb.getCallback().onSuccess(reply);

                // save token again
                OkHttpUtils.setToken(reply.getAccessToken());
                // auto connect
                Api.getWsPushService().connect(reply.getAccessToken(), Api.getOptions().gatewayAddress);
            }
        });

        return cb;
    }

    @Override
    public void observeOnlineStatus(Observer<StatusCode> observer, boolean register) {
        // Api.getWsPushService().observeOnlineStatus(observer, register);
    }
}
