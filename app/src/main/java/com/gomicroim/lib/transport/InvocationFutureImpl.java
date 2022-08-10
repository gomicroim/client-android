package com.gomicroim.lib.transport;

public class InvocationFutureImpl<T> implements InvocationFuture<T> {
    private RequestCallback<T> callback;

    @Override
    public void setCallback(RequestCallback<T> callback) {
        this.callback = callback;
    }

    public RequestCallback<T> getCallback() {
        return this.callback;
    }
}
