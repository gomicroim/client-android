package com.gomicroim.lib.helper;

import org.json.JSONException;

public interface HttpResponseCallBack {
    void onSuccess(String json) throws JSONException;

    void onException(Throwable exception);
}
