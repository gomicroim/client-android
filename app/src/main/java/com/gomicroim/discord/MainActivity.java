package com.gomicroim.discord;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gomicroim.lib.Api;
import com.gomicroim.lib.model.constant.StatusCode;
import com.gomicroim.lib.protos.Constants;
import com.gomicroim.lib.protos.websocket.Websocket;
import com.gomicroim.lib.service.WsPushListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements WsPushListener {
    private static final String TAG = "MainActivity";
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

        lvMsgAdapter = new ArrayAdapter<>(this, R.layout.activity_listview, R.id.textView, lvMsgArr);
        lvMsg.setAdapter(lvMsgAdapter);

        Api.getWsPushService().observer(this, true);
    }

    @Override
    public void onMessage(Websocket.S2CWebsocketMessage message) {
        runOnUiThread(() -> {
            StringBuilder builder = new StringBuilder();

            builder.append("seq:").append(message.getHeader().getSeq());
            builder.append(",dataList:[");
            for (Websocket.WebsocketMessage i : message.getDataListList()) {
                if (i.getMsgType() == Constants.WSMessageType.NewChatMessage) {
                    
                } else {
                    builder.append("wsMsgType:").append(i.getMsgType()).
                            append(",len:").append(i.getAnyData().toByteArray().length);
                }
            }
            builder.append("]");
            lvMsgArr.add(builder.toString());

            // update listview
            lvMsgAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onStatusCodeChanged(StatusCode before, StatusCode after) {
        runOnUiThread(() -> {
            switch (after) {
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
        });
    }
}