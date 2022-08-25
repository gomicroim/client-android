package com.gomicroim.lib;

import com.gomicroim.lib.model.dto.LoginReply;
import com.gomicroim.lib.protos.user.User;
import com.gomicroim.lib.transport.RequestCallback;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginServiceTest {
    private static final String TAG = "TestDeviceRegister";
    private static Logger log = LoggerFactory.getLogger(LoginServiceTest.class);

    @Test
    public void TestDeviceRegister() throws InterruptedException {
        Api.init(ApiOptions.DEFAULT, null);
        User.RegisterRequest req = User.RegisterRequest.newBuilder().setDeviceId("dddd").setOsVersion("android").build();
        Api.getLoginService().deviceRegister(req).setCallback(new RequestCallback<User.RegisterReply>() {
            @Override
            public void onSuccess(User.RegisterReply param) {
                log.info("onSuccess: " + param.toString());
            }

            @Override
            public void onFailed(int code, String message) {

            }

            @Override
            public void onException(Throwable exception) {
                log.info("v: " + exception.getMessage());
            }
        });
        Thread.sleep(5000);
    }

    @Test
    public void TestAuthLogin() throws InterruptedException {
        Api.init(ApiOptions.DEFAULT, null);
        User.RegisterRequest req = User.RegisterRequest.newBuilder().setDeviceId("dddd").setOsVersion("android").build();
        Api.getLoginService().deviceRegister(req);
        Thread.sleep(1000);
        Api.getLoginService().login("8617300000000", "000000", "1.0").setCallback(new RequestCallback<User.AuthReply>() {
            @Override
            public void onSuccess(User.AuthReply param) {
                log.info("onSuccess: " + param.toString());
            }

            @Override
            public void onFailed(int code, String message) {
            }

            @Override
            public void onException(Throwable exception) {
                log.info("v: " + exception.getMessage());
            }
        });
        Thread.sleep(2000);
    }
}
