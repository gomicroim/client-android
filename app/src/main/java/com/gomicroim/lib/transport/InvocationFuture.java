package com.gomicroim.lib.transport;

/**
 * 设置结果回调的接口
 */
public interface InvocationFuture<T> {
    /**
     * 设置结果回调
     *
     * @param callback: 回调结果
     */
    void setCallback(RequestCallback<T> callback);
}