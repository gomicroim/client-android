package com.gomicroim.lib.service;

import com.gomicroim.lib.model.dto.ReceiverInfo;
import com.gomicroim.lib.protos.chat.Chat;
import com.gomicroim.lib.transport.InvocationFuture;

public interface ChatService {
    /**
     * 发送消息
     *
     * @param msgRequest: 请求
     * @return 结果回调
     */
    InvocationFuture<Chat.SendMsgReply> sendMsg(Chat.SendMsgRequest msgRequest);

    /**
     * 构建文本消息
     *
     * @param to:   目标Id
     * @param text: 文本内容
     * @return 文本消息
     */
    Chat.SendMsgRequest buildTextMsg(ReceiverInfo to, String text);

    /**
     * 查询历史消息
     *
     * @param peer      信息
     * @param msgSeq    消息序号，isForward=true时代表结束消息序号（不包含），否则代表其实消息需求（不包含）
     * @param isForward 扫描放心，true：向前扫描，false：向后扫描
     * @return 消息列表
     */
    InvocationFuture<Chat.GetMsgListReply> getMsgList(ReceiverInfo peer, long msgSeq, boolean isForward, int limitCount);

    /**
     * 查询最近历史会话记录
     *
     * @param request
     * @return
     */
//    InvocationFuture<Chat.GetRecentSessionReply> getRecentContactSession(Chat.GetRecentSessionRequest request);
}
