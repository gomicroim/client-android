package com.gomicroim.discord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gomicroim.lib.Api;
import com.gomicroim.lib.model.constant.StatusCode;
import com.gomicroim.lib.service.WsPushListener;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements WsPushListener, View.OnClickListener {
    private ListView lvMsg;
    private TextView tvNetwork;
    private ArrayList<String> lvMsgArr = new ArrayList<>();
    private ArrayAdapter<String> lvMsgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMsg = findViewById(R.id.lvMsg);
        tvNetwork = findViewById(R.id.tvNetwork);

        findViewById(R.id.btn_c2c).setOnClickListener(this);
        findViewById(R.id.btn_c2g).setOnClickListener(this);

        lvMsgAdapter = new ArrayAdapter<>(this, R.layout.activity_listview, R.id.textView, lvMsgArr);
        lvMsg.setAdapter(lvMsgAdapter);

        Api.getWsPushService().observer(this, true);
        loadConnectStatus(Api.getWsPushService().getStatusCode());
    }

    @Override
    public void onMessage(Message message) {
        /*
        runOnUiThread(() -> {
            StringBuilder builder = new StringBuilder();

            builder.append("[").append(message.getHeader().getSeq());
            builder.append("],data:[");
            for (Websocket.WebsocketMessage i : message.getDataListList()) {
                if (i.getMsgType() == Constants.WSMessageType.NewChatMessage) {
                    try {
                        Websocket.Message textMsg = i.getAnyData().unpack(Websocket.Message.class);
                        builder.append("{from:").append(textMsg.getUserId()).
                                append(",room:").append(textMsg.getRoom().getRoomId()).
                                append(",channelId:").append(textMsg.getChannel().getChannelId()).
                                append(",text:").append(textMsg.getContent());
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.append("wsMsgType:").append(i.getMsgType()).
                            append(",len:").append(i.getAnyData().toByteArray().length);
                }
                builder.append("},");
            }
            builder.append("]");
            lvMsgArr.add(builder.toString());

            // update listview
            lvMsgAdapter.notifyDataSetChanged();
        });
        */
    }

    @Override
    public void onStatusCodeChanged(StatusCode before, StatusCode after) {
        runOnUiThread(() -> {
            loadConnectStatus(after);
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_c2c) {
            startActivity(new Intent(MainActivity.this, C2CActivity.class));
        } else if (v.getId() == R.id.btn_c2g) {
            startActivity(new Intent(MainActivity.this, C2GActivity.class));
        }
    }

    private void loadConnectStatus(StatusCode code) {
        switch (code) {
            case NET_BROKEN:
                tvNetwork.setText("网络连接中断");
                break;
            case LOGINED:
                tvNetwork.setText("登录成功");
                break;
            case LOGINING:
                tvNetwork.setText("登录中...");
                break;
            case CONNECTING:
                tvNetwork.setText("连接中...");
                break;
            case UN_LOGIN:
                tvNetwork.setText("登录失败");
                break;
            default:
                tvNetwork.setText("未知");
                break;
        }
    }
}