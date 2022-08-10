package com.gomicroim.lib;

import com.gomicroim.lib.helper.OkHttpUtils;
import com.gomicroim.lib.service.LoginService;
import com.gomicroim.lib.service.LoginServiceImpl;

import okhttp3.OkHttpClient;

/**
 * API封装，对外暴露所有接口
 */
public class Api {
    private static ApiOptions apiOptions = ApiOptions.DEFAULT;
    private static LoginService loginService;

    /**
     * 获取API配置信息
     *
     * @return 只读值
     */
    public static ApiOptions getOptions() {
        ApiOptions temp = new ApiOptions();
        temp.apiServerAddress = apiOptions.apiServerAddress;
        temp.gatewayAddress = apiOptions.gatewayAddress;
        return temp;
    }

    /**
     * 初始化API
     *
     * @param options 配置信息
     */
    public static void init(ApiOptions options) {
        apiOptions = options;
        OkHttpUtils.setBaseUrl(apiOptions.apiServerAddress);
    }

    /**
     * 获取 登录 Service实例
     *
     * @return 实例
     */
    public static LoginService getLoginService() {
        return new LoginServiceImpl();
    }
}
