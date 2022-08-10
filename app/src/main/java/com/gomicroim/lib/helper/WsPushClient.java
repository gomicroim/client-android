package com.gomicroim.lib.helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class WsPushClient extends WebSocketListener {
    private WebSocket webSocket;
    private Logger log = LoggerFactory.getLogger(WsPushClient.class);

    /**
     * connect the remote server
     *
     * @param wsUrl webSocket server url
     */
    public void connect(String wsUrl) {
        OkHttpClient client = new OkHttpClient.Builder()
                .pingInterval(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(wsUrl)
                .build();
        webSocket = client.newWebSocket(request, this);
    }

    /**
     * Active disconnection
     *
     * @param code   close code
     * @param reason close reason
     */
    public void disconnect(int code, String reason) {
        if (webSocket != null)
            webSocket.close(code, reason);
    }

    /**
     * Send string message
     *
     * @param message 字符串
     */
    public void send(final String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    /**
     * Send a byte message
     *
     * @param message 字节数组
     */
    public void send(final ByteString message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        log.info("client received message" + text);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        log.info("connection successful!");
    }
}
