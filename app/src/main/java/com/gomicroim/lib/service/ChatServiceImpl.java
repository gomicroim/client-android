package com.gomicroim.lib.service;

import static com.gomicroim.lib.ApiUrl.URL_CHAT_GET_MSG_LIST;

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
        ApiHelper<Chat.GetMsgListReply> chat = new ApiHelper<>();
        Chat.GetMsgListRequest req = Chat.GetMsgListRequest.newBuilder().
                setPeerId(peer.toString()).setIsForward(isForward).setMsgSeq(msgSeq).setLimitCount(limitCount).build();
        return chat.get(URL_CHAT_GET_MSG_LIST, req, Chat.GetMsgListReply.newBuilder());
    }
}
