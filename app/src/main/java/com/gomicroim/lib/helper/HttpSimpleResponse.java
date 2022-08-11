package com.gomicroim.lib.helper;

import com.gomicroim.lib.transport.InvocationFuture;
import com.gomicroim.lib.transport.RequestCallback;

/**
 * 抽象类。对HttpResponseCallBack简化，只关注成功逻辑的解析即可，错误则直接透传
 *
 * @param <T>
 */
public abstract class HttpSimpleResponse<T> implements HttpResponseCallBack {
    private final InvocationFuture<T> callback;

    public HttpSimpleResponse(InvocationFuture<T> cb) {
        callback = cb;
    }

    @Override
    public void onFailed(int httpCode, String msg, String body) {
        callback.getCallback().onFailed(httpCode, msg);
    }

    @Override
    public void onException(Throwable exception) {
        callback.getCallback().onException(exception);
    }
}
