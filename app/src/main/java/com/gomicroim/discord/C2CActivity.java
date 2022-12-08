package com.gomicroim.discord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gomicroim.lib.Api;
import com.gomicroim.lib.model.dto.ReceiverInfo;
import com.gomicroim.lib.protos.chat.Chat;
import com.gomicroim.lib.protos.chat.ChatContant;
import com.gomicroim.lib.transport.RequestCallback;

import java.util.ArrayList;

public class C2CActivity extends AppCompatActivity implements View.OnClickListener, RequestCallback<Chat.GetMsgListReply> {
    private EditText etToUserId;
    private EditText etMsgText;
    private ArrayList<String> lvMsgArr = new ArrayList<>();
    private ArrayAdapter<String> lvMsgAdapter;

    // 设一个小值，方便观察循环拉取消息
    private final int kLimitPageCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c2c);
        findViewById(R.id.btn_c2c_send).setOnClickListener(this);
        findViewById(R.id.btn_c2c_query_history).setOnClickListener(this);

        etToUserId = findViewById(R.id.et_c2c_peer_id);
        etMsgText = findViewById(R.id.et_c2c_msg_text);
        ListView lv = findViewById(R.id.lv_c2c_msg);

        lvMsgAdapter = new ArrayAdapter<>(this, R.layout.activity_listview, R.id.textView, lvMsgArr);
        lv.setAdapter(lvMsgAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_c2c_send) {
            onClickSend();
        } else if (v.getId() == R.id.btn_c2c_query_history) {
            onClickQueryHistory();
        }
    }

    public void onClickSend() {
        long userId = getUserId();
        if (userId == 0) {
            return;
        }

        String text = etMsgText.getText().toString();
        if (text.isEmpty()) {
            Toast.makeText(this, "please input msg text!", Toast.LENGTH_SHORT).show();
            return;
        }

        Chat.SendMsgRequest req = Api.getChatService().
                buildTextMsg(new ReceiverInfo(userId, ChatContant.IMSessionType.kCIM_SESSION_TYPE_SINGLE), text);


        Context ctx = this;
        Api.getChatService().sendMsg(req).setCallback(new RequestCallback<Chat.SendMsgReply>() {
            @Override
            public void onSuccess(Chat.SendMsgReply param) {
                runOnUiThread(() -> {
                    appendMsg(param.getMsgInfo());
                });
            }

            @Override
            public void onFailed(int code, String message, Throwable exception) {
                Toast.makeText(ctx, "send failed:" + message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onClickQueryHistory() {
        long userId = getUserId();
        if (userId == 0) {
            return;
        }

        lvMsgArr.clear();
        lvMsgAdapter.notifyDataSetChanged();

        long endMsgSeq = Long.MAX_VALUE;
        Api.getChatService().getMsgList(new ReceiverInfo(userId, ChatContant.IMSessionType.kCIM_SESSION_TYPE_SINGLE),
                endMsgSeq, true, kLimitPageCount).setCallback(this);
    }

    private void appendMsg(Chat.IMMsgInfo msgInfo) {
        String builder = msgInfo.getFromUserId() +
                " -> " +
                msgInfo.getPeerId() +
                ": " +
                msgInfo.getMsgData();

        this.lvMsgArr.add(builder);
        this.lvMsgAdapter.notifyDataSetChanged();
    }

    private long getUserId() {
        long userId = 0;
        try {
            userId = Long.parseLong(etToUserId.getText().toString());
            if (userId == 0) {
                Toast.makeText(this, "userId must not be 0", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "userId is empty or not number", Toast.LENGTH_SHORT).show();
        }
        return userId;
    }

    @Override
    public void onSuccess(Chat.GetMsgListReply param) {
        runOnUiThread(() -> {
            if (param.getEndMsgSeq() == Long.MAX_VALUE && param.getMsgListList().size() <= 0) {
                Toast.makeText(this, "未查询到历史聊天记录", Toast.LENGTH_SHORT).show();
                return;
            }
            for (Chat.IMMsgInfo msg : param.getMsgListList()) {
                appendMsg(msg);
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        // 继续拉取
        if (param.getMsgListCount() != 0) {
            long userId = getUserId();
            if (userId == 0) {
                return;
            }
            Api.getChatService().getMsgList(new ReceiverInfo(userId, ChatContant.IMSessionType.kCIM_SESSION_TYPE_SINGLE),
                    param.getEndMsgSeq(), true, kLimitPageCount).setCallback(this);
        }
    }

    @Override
    public void onFailed(int code, String message, Throwable exception) {
        runOnUiThread(() -> {
            Toast.makeText(this, "failed:" + message, Toast.LENGTH_LONG).show();
        });
    }
}