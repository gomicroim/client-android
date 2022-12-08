package com.gomicroim.lib;

import org.jetbrains.annotations.NotNull;

/**
 * 全局API配置
 */
public class ApiOptions {
    @NotNull
    public static final ApiOptions DEFAULT = new ApiOptions();

    public ApiOptions() {
    }

    // 10.0.2.2 从模拟器访问本机服务
    public String apiServerAddress = "http://10.0.2.2:8888";
    public String gatewayAddress = "ws://10.0.2.2:8102/ws";
}
