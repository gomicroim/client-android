package com.gomicroim.lib.service;

import com.gomicroim.lib.model.constant.StatusCode;
import com.google.protobuf.Message;

/**
 * ws推送观察器
 */
public interface WsPushListener {
    /**
     * 收到服务器发来的数据
     *
     * @param message pb数据
     */
    void onMessage(Message message);

    /**
     * 状态改变通知
     *
     * @param before: 改变前的状态
     * @param after:  改变后的状态
     */
    void onStatusCodeChanged(StatusCode before, StatusCode after);
}
