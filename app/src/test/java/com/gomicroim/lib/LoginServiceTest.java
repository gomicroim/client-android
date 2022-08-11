package com.gomicroim.lib;

import com.gomicroim.lib.model.dto.DeviceReply;
import com.gomicroim.lib.model.dto.DeviceReq;
import com.gomicroim.lib.model.dto.LoginReply;
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
        Api.getLoginService().deviceRegister(new DeviceReq()).setCallback(new RequestCallback<DeviceReply>() {
            @Override
            public void onSuccess(DeviceReply param) {
                log.info("onSuccess: " + param.toString());
            }

            @Override
            public void onFailed(int code) {
                log.info("onFailed: " + code);
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
        Api.getLoginService().deviceRegister(new DeviceReq());
        Thread.sleep(1000);
        Api.getLoginService().login("8617300000000", "000000", "1.0").setCallback(new RequestCallback<LoginReply>() {
            @Override
            public void onSuccess(LoginReply param) {
                log.info("onSuccess: " + param.toString());
            }

            @Override
            public void onFailed(int code) {
                log.info("onFailed: " + code);
            }

            @Override
            public void onException(Throwable exception) {
                log.info("v: " + exception.getMessage());
            }
        });
        Thread.sleep(2000);
    }
}
