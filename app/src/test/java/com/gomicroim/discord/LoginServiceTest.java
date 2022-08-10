package com.gomicroim.discord;

import com.gomicroim.lib.Api;
import com.gomicroim.lib.ApiOptions;
import com.gomicroim.lib.model.dto.DeviceReply;
import com.gomicroim.lib.model.dto.DeviceReq;
import com.gomicroim.lib.transport.RequestCallback;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginServiceTest {
    private static final String TAG = "TestDeviceRegister";
    private static Logger log = LoggerFactory.getLogger(LoginServiceTest.class);

    @Test
    public void TestDeviceRegister() throws InterruptedException {
        Api.init(ApiOptions.DEFAULT);
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
}
