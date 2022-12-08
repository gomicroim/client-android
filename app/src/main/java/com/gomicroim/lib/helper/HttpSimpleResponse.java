package com.gomicroim.lib.helper;

import android.os.Message;

import com.gomicroim.lib.transport.InvocationFuture;
import com.gomicroim.lib.transport.RequestCallback;
import com.gomicroim.lib.util.ProtoJsonUtils;
import com.google.gson.JsonSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 抽象类。对HttpResponseCallBack简化，只关注成功逻辑的解析即可，错误则直接透传
 *
 * @param <T>
 */
public abstract class HttpSimpleResponse<T> implements HttpResponseCallBack {
    private final InvocationFuture<T> callback;
    private Logger log = LoggerFactory.getLogger(HttpSimpleResponse.class);

    public HttpSimpleResponse(InvocationFuture<T> cb) {
        callback = cb;
    }

    @Override
    public void onFailed(int httpCode, String msg, Throwable exception) {
        callback.getCallback().onFailed(httpCode, msg, exception);
    }
}
