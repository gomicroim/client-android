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

    /**
     * 获取设置的结果回调
     *
     * @return 回调
     */
    RequestCallback<T> getCallback();
}