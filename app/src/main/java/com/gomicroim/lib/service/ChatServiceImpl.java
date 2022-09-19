package com.gomicroim.lib.service;

import com.gomicroim.lib.ApiUrl;
import com.gomicroim.lib.helper.ApiHelper;
import com.gomicroim.lib.model.dto.ReceiverInfo;
import com.gomicroim.lib.protos.chat.Chat;
import com.gomicroim.lib.protos.chat.ChatContant;
import com.gomicroim.lib.transport.InvocationFuture;

import java.util.UUID;

public class ChatServiceImpl implements ChatService {
    @Override
    public InvocationFuture<Chat.SendMsgReply> sendMsg(Chat.SendMsgRequest msgRequest) {
        ApiHelper<Chat.SendMsgReply> chat = new ApiHelper<>();
        return chat.post(ApiUrl.URL_CHAT_MSG_SEND, msgRequest, Chat.SendMsgReply.newBuilder());
    }

    @Override
    public Chat.SendMsgRequest buildTextMsg(ReceiverInfo to, String text) {
        return Chat.SendMsgRequest.newBuilder().
                setClientMsgId(UUID.randomUUID().toString()).
                setTo(to.toString()).
                setMsgType(ChatContant.IMMsgType.kCIM_MSG_TYPE_TEXT).
                setMsgData(text).build();
    }

    @Override
    public InvocationFuture<Chat.GetMsgListReply> getMsgList(ReceiverInfo peer, long msgSeq, boolean isForward, int limitCount) {
        return null;
    }
}
