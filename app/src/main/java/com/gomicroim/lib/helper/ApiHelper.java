package com.gomicroim.lib.helper;

import com.gomicroim.lib.transport.InvocationFutureImpl;
import com.gomicroim.lib.util.ProtoJsonUtils;
import com.google.gson.JsonSyntaxException;
import com.google.protobuf.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 发送 http 请求简化类
 */
public class ApiHelper<T extends Message> {
    private Logger log = LoggerFactory.getLogger(ApiHelper.class);

    /**
     * POST 请求，入参是protobuf，出参也是，自动转换成json
     *
     * @param url         接口url
     * @param param       请求参数
     * @param resultBuild 返回参数转换成protobuf的结构 Builder对象
     * @return 异步回调接口
     */
    public InvocationFutureImpl<T> post(String url, Message param, Message.Builder resultBuild) {
        InvocationFutureImpl<T> cb = new InvocationFutureImpl<>();

        String json = "";
        try {
            json = ProtoJsonUtils.toJson(param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpUtils.postAsyncJson(url, json,
                new HttpSimpleResponse<T>(cb) {
                    @Override
                    public void onSuccess(String json) throws JsonSyntaxException, IOException {
                        T result = (T) ProtoJsonUtils.toProtoBean(resultBuild, json);
                        log.info("post success, result:{}", result);
                        cb.getCallback().onSuccess(result);
                    }
                });
        return cb;
    }
}
