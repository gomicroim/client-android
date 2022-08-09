package com.gomicroim.lib.transport;

import org.json.JSONException;

public interface HttpResponseCallBack {
    void success(String json) throws JSONException;
    
    void error(String json);
}
