package com.gomicroim.lib.transport;

public class InvocationFutureImpl<T> implements InvocationFuture<T> {
    private RequestCallback<T> callback;

    @Override
    public void setCallback(RequestCallback<T> callback) {
        this.callback = callback;
    }

    public RequestCallback<T> getCallback() {
        // default empty
        if (this.callback == null) {
            this.callback = new RequestCallback<T>() {
                @Override
                public void onSuccess(T param) {
                }

                @Override
                public void onFailed(int code) {
                }

                @Override
                public void onException(Throwable exception) {
                }
            };
        }
        return this.callback;
    }
}
