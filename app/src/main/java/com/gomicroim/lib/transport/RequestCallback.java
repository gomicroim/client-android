package com.gomicroim.lib.transport;

public interface RequestCallback<T> {
    void onSuccess(T param);

    void onFailed(int code, String message, Throwable exception);
}
