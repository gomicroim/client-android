package com.gomicroim.lib.helper;

import com.google.gson.JsonSyntaxException;

public interface HttpResponseCallBack {
    void onSuccess(String json) throws JsonSyntaxException;

    void onException(Throwable exception);
}
