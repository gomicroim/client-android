package com.gomicroim.discord;

import android.util.Log;

import com.gomicroim.lib.Api;
import com.gomicroim.lib.ApiOptions;
import com.gomicroim.lib.model.dto.DeviceReply;
import com.gomicroim.lib.model.dto.DeviceReq;
import com.gomicroim.lib.transport.RequestCallback;

import org.junit.Test;

public class LoginServiceTest {
    private static final String TAG = "TestDeviceRegister";

    @Test
    public void TestDeviceRegister() {
        Api.init(ApiOptions.DEFAULT);
        Api.getLoginService().deviceRegister(new DeviceReq()).setCallback(new RequestCallback<DeviceReply>() {

            @Override
            public void onSuccess(DeviceReply param) {
                Log.i(TAG, "onSuccess: " + param.toString());
            }

            @Override
            public void onFailed(int code) {
                Log.i(TAG, "onFailed: " + code);
            }

            @Override
            public void onException(Throwable exception) {
                Log.i(TAG, "v: " + exception.getMessage());
            }
        });
    }
}
