package com.gomicroim.discord.helper;

import android.content.Context;

import com.gomicroim.discord.util.SharedPreferencesUtils;

public class SharedPreferencesHelper {
    private SharedPreferencesUtils preferencesUtils;

    private static final String KEY_PHONE = "phone";
    private static final String KEY_PWD_CHECK = "rememberPwdChecked";
    private static final String KEY_DEVICE_ID = "deviceId";

    public SharedPreferencesHelper(Context context) {
        preferencesUtils = new SharedPreferencesUtils(context);
    }

    public void saveRememberPwdChecked(boolean checked) {
        preferencesUtils.putValues(new SharedPreferencesUtils.ContentValue(KEY_PWD_CHECK, checked));
    }

    public boolean readRememberPwdChecked() {
        return preferencesUtils.getBoolean(KEY_PWD_CHECK, false);
    }

    public void savePhone(String phone) {
        preferencesUtils.putValues(new SharedPreferencesUtils.ContentValue(KEY_PHONE, phone));
    }

    public String readPhone() {
        return preferencesUtils.getString(KEY_PHONE);
    }

    public void saveDeviceId(String deviceId) {
        preferencesUtils.putValues(new SharedPreferencesUtils.ContentValue(KEY_DEVICE_ID, deviceId));
    }

    public String readDeviceId() {
        return preferencesUtils.getString(KEY_DEVICE_ID);
    }
}
