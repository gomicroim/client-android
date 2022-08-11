package com.gomicroim.lib.service;

import com.gomicroim.lib.Observer;
import com.gomicroim.lib.helper.OkHttpUtils;
import com.gomicroim.lib.model.constant.StatusCode;
import com.gomicroim.lib.protos.websocket.Websocket;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * webSocket封装
 */
public class WsPushServiceImpl extends WebSocketListener implements WsPushService {
    private WebSocket webSocket;
    private StatusCode statusCode;
    private final Logger log = LoggerFactory.getLogger(WsPushServiceImpl.class);
    private final LinkedList<WsPushListener> listeners = new LinkedList<>();

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        log.info("onClosed, code:{}, reason:{}", code, reason);

        updateStatusCode(StatusCode.NET_BROKEN);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);

        String bodyStr = "";
        try {
            assert response != null;
            bodyStr = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("onFailure, response: {},body: {}", response, bodyStr);

        updateStatusCode(StatusCode.UN_LOGIN);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
        log.debug("onMessage");
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        log.info("connection successful!");
        updateStatusCode(StatusCode.LOGINED);
    }

    // WsPushService

    @Override
    public void connect(String token, String wsUrl) {
        disconnect();
        updateStatusCode(StatusCode.CONNECTING);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .pingInterval(30, TimeUnit.SECONDS)   // 心跳ping/pong时间
                .build();
        Request request = new Request.Builder()
                .url(wsUrl)
                .addHeader("authorization", "Bearer " + token)
                .build();
        webSocket = client.newWebSocket(request, this);
    }

    @Override
    public void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "manual close");
            webSocket = null;
            updateStatusCode(StatusCode.NET_BROKEN);
        }
    }

    @Override
    public StatusCode getStatusCode() {
        return statusCode;
    }

    @Override
    public void send(Websocket.C2SWebsocketMessage message) {
        byte[] data = message.toByteArray();
        webSocket.send(ByteString.of(data));
    }

    @Override
    public void observer(WsPushListener listener, boolean register) {
        if (register) {
            listeners.add(listener);
        } else {
            listeners.remove(listener);
        }
    }

    private void updateStatusCode(StatusCode code) {
        StatusCode before = statusCode;
        if (before == code) {
            return;
        }
        statusCode = code;

        for (WsPushListener item : listeners) {
            item.onStatusCodeChanged(before, code);
        }
    }
}
