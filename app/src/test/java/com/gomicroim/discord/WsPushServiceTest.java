package com.gomicroim.discord;

import com.gomicroim.lib.Api;
import com.gomicroim.lib.ApiOptions;
import com.gomicroim.lib.model.constant.StatusCode;
import com.gomicroim.lib.service.WsPushService;

import org.junit.Test;

public class WsPushServiceTest {

    private WsPushService setup() {
        String accessToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiNDE4MzgiLCJjbGllbnRfaWQiOiI2MGQ4ZDQ1NC00YjRkLTQzNDItYWJjMS0xZGQwNzdlNzg5YTkiLCJleHBpcmVzX2F0IjoxNjYyNzgwNzcwLCJ0eXBlIjoiYWNjZXNzIn0.EuX4Nddxjrq5sLqAB7rL5Dv11FxovlRh9gQYhnqLc3fWX1o_k-NRPH6pEOpqwiVOo1uD-Smx9U95nC0raY3EHQ";
        Api.init(ApiOptions.DEFAULT, null);

        WsPushService wsPushService = Api.getWsPushService();
        wsPushService.connect(accessToken, "ws://127.0.0.1:8000/websocket");
        return wsPushService;
    }

    @Test
    public void TestWebSocketAuth() throws InterruptedException {
        WsPushService wsPushService = setup();
        Thread.sleep(2000);

        assert wsPushService.getStatusCode() == StatusCode.LOGINED;
    }

    @Test
    public void TestReconnect() throws InterruptedException {
        WsPushService client = setup();
        Thread.sleep(60 * 1000);
    }
}
