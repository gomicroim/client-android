package com.gomicroim.lib.helper;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;

public interface HttpResponseCallBack {
    void onSuccess(String json) throws JsonSyntaxException, IOException;

    void onFailed(int httpCode, String msg, String body);

    void onException(Throwable exception);
}
