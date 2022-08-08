package com.gomicroim.lib;

import com.gomicroim.lib.service.LoginService;

/**
 * API封装，对外暴露所有接口
 */
public class Api {
    private static Api api = new Api();

    private LoginService loginService;

    /**
     * 获取单例
     */
    public Api getInstance() {
        return api;
    }

    public Api(){

    }
}
