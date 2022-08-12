package com.gomicroim.discord;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gomicroim.lib.Api;
import com.gomicroim.lib.model.constant.StatusCode;
import com.gomicroim.lib.protos.websocket.Websocket;
import com.gomicroim.lib.service.WsPushListener;

public class MainActivity extends AppCompatActivity implements WsPushListener {
    private ListView lvMsg;
    private TextView tvNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lvMsg = findViewById(R.id.lvMsg);
        tvNetwork = findViewById(R.id.tvNetwork);

        //Api.getWsPushService().observer(this, true);
    }

    @Override
    public void onMessage(Websocket.S2CWebsocketMessage message) {

    }

    @Override
    public void onStatusCodeChanged(StatusCode before, StatusCode after) {
        runOnUiThread(() -> {
            switch (after) {
                case NET_BROKEN:
                    tvNetwork.setText("网络连接中断");
                case LOGINED:
                    tvNetwork.setText("登录成功");
                case LOGINING:
                    tvNetwork.setText("登录中...");
                case CONNECTING:
                    tvNetwork.setText("连接中...");
                case UN_LOGIN:
                    tvNetwork.setText("登录失败");
                default:
                    tvNetwork.setText("未知");
            }
        });
    }
}