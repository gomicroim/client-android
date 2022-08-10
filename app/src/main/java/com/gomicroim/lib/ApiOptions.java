package com.gomicroim.lib;

import androidx.annotation.NonNull;

/**
 * 全局API配置
 */
public class ApiOptions {
    @NonNull
    public static final ApiOptions DEFAULT = new ApiOptions();

    public ApiOptions() {
    }

    public String apiServerAddress = "http://127.0.0.1:9000";
    public String gatewayAddress = "ws://127.0.0.1:8000/websocket";
}
