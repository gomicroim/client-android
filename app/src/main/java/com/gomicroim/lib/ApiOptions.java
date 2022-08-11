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

    //public String apiServerAddress = "http://192.168.200.197:9000";
    //public String gatewayAddress = "ws://192.168.200.197:8000/websocket";
    public String apiServerAddress = "https://api.dev.shuodev.com";
    public String gatewayAddress = "wss://ws-gw.dev.shuodev.com/websocket";
}
