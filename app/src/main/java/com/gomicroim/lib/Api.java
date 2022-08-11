package com.gomicroim.lib;

import com.gomicroim.lib.helper.OkHttpUtils;
import com.gomicroim.lib.service.LoginService;
import com.gomicroim.lib.service.LoginServiceImpl;
import com.gomicroim.lib.service.WsPushService;
import com.gomicroim.lib.service.WsPushServiceImpl;

/**
 * API封装，对外暴露所有接口
 */
public class Api {
    private static ApiOptions apiOptions = ApiOptions.DEFAULT;
    private static final LoginService loginService = new LoginServiceImpl();
    private static final WsPushService wsPushService = new WsPushServiceImpl();

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
     * 初始化API（启动后台服务，若已经存在用户登录信息， SDK 将进行自动登录）
     *
     * @param options   配置信息
     * @param loginInfo 如果设置该值，则将自动登录
     */
    public static void init(ApiOptions options, LoginInfo loginInfo) {
        apiOptions = options;
        OkHttpUtils.setBaseUrl(apiOptions.apiServerAddress);
        if (loginInfo != null) {
            OkHttpUtils.setToken(loginInfo.getToken());
            // auto connect
            Api.getWsPushService().connect(loginInfo.getToken(), Api.getOptions().gatewayAddress);
        }
    }

    /**
     * 获取 登录 Service实例
     *
     * @return 实例
     */
    public static LoginService getLoginService() {
        return loginService;
    }

    /**
     * 获取 WS连接 Service实例
     *
     * @return 实例
     */
    public static WsPushService getWsPushService() {
        return wsPushService;
    }
}
