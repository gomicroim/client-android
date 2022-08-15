package com.gomicroim.lib.service;

import android.annotation.SuppressLint;

import com.gomicroim.lib.Observer;
import com.gomicroim.lib.helper.OkHttpUtils;
import com.gomicroim.lib.model.constant.StatusCode;
import com.gomicroim.lib.protos.websocket.Websocket;
import com.google.protobuf.InvalidProtocolBufferException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * webSocket封装
 */
public class WsPushServiceImpl extends WebSocketListener implements WsPushService, Runnable {
    private WebSocket webSocket;
    private StatusCode statusCode;
    private String token;
    private String wsUrl;

    // 重连
    private boolean reConnect = true;
    private long reConnectCount = 0;
    private long lastSendPingTime = 0;

    private static final long PING_INTERVAL = 30; // 30 s
    private static final long MAX_RE_CONNECT_WAIT_TIME = 4;

    private final Logger log = LoggerFactory.getLogger(WsPushServiceImpl.class);
    private final LinkedList<WsPushListener> listeners = new LinkedList<>();

    public WsPushServiceImpl() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        log.info("onClosed, code:{}, reason:{}", code, reason);

        updateStatusCode(StatusCode.NET_BROKEN);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        int httpCode = 0;
        String httpMsg = "";

        String bodyStr = "";
        try {
            if (response != null) {
                if (response.body() != null) {
                    bodyStr = response.body().string();
                }
                httpCode = response.code();
                httpMsg = response.message();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("onFailure, response: {},body: {}, code: {}, msg: {}, Throwable:{}", response, bodyStr,
                httpCode, httpMsg, t);

        updateStatusCode(StatusCode.UN_LOGIN);

        if (!bodyStr.equals("")) {
            reConnect = false;
            return;
        }
        reConnect();
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);

        Websocket.S2CWebsocketMessage msg;
        try {
            msg = Websocket.S2CWebsocketMessage.parseFrom(bytes.toByteArray());
            log.debug("onMessage,seq:{},dataListLen:{}", msg.getHeader().getSeq(), msg.getDataListCount());
            for (WsPushListener item : listeners) {
                item.onMessage(msg);
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
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
        reConnect = true;
        this.token = token;
        this.wsUrl = wsUrl;

        log.info("connect wsServer: {}", wsUrl);

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

    private void reConnect() {
        if (reConnect) {
            reConnectCount++;
            if (reConnectCount > MAX_RE_CONNECT_WAIT_TIME) {
                reConnectCount = 1;
            }
            // 2s 4s 8s 16s
            double sleepTime = Math.pow(2, reConnectCount) * 1000;
            try {
                Thread.sleep((long) sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (reConnect) {
                connect(this.token, this.wsUrl);
            }
        }
    }

    @Override
    public void disconnect() {
        reConnect = false;
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
        log.debug("updateStatusCode, before:{}, after:{}", before, code);

        for (WsPushListener item : listeners) {
            item.onStatusCodeChanged(before, code);
        }
    }

    // ws ping thread
    @Override
    public void run() {
        long curTime = System.currentTimeMillis() / 1000;
        if (lastSendPingTime - curTime >= PING_INTERVAL) {
            lastSendPingTime = curTime;
            // webSocket.send()
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
