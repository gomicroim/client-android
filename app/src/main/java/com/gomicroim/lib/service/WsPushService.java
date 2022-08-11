package com.gomicroim.lib.service;

import com.gomicroim.lib.Observer;
import com.gomicroim.lib.model.constant.StatusCode;
import com.gomicroim.lib.protos.websocket.Websocket;

public interface WsPushService {
    /**
     * connect the remote server
     *
     * @param wsUrl webSocket server url
     */
    void connect(String token, String wsUrl);

    /**
     * 断开连接
     */
    void disconnect();

    /**
     * 获取当前连接状态
     *
     * @return 连接状态
     */
    StatusCode getStatusCode();

    /**
     * Send a byte message
     *
     * @param message 字节数组
     */
    void send(final Websocket.C2SWebsocketMessage message);

    /**
     * 注册观察者
     *
     * @param listener 回调
     * @param register true: 注册， false: 注销
     */
    void observer(WsPushListener listener, boolean register);
}
