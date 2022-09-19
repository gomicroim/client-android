package com.gomicroim.lib.model.dto;

import com.gomicroim.lib.protos.chat.ChatContant;

/**
 * 接收者信息。
 * 用户ID：user-
 * 群ID：group-
 */
public class ReceiverInfo {
    private long id;
    public ChatContant.IMSessionType sessionType;

    public ReceiverInfo(long id, ChatContant.IMSessionType sessionType) {
        this.id = id;
        this.sessionType = sessionType;
    }

    @Override
    public String toString() {
        if (sessionType == ChatContant.IMSessionType.kCIM_SESSION_TYPE_GROUP) {
            return "group-" + id;
        }
        return Long.toString(id);
    }
}
