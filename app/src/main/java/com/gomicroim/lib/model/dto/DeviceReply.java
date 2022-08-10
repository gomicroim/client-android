package com.gomicroim.lib.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceReply implements Serializable {
    @SerializedName("guestToken")
    public String guestToken;

    @Override
    public String toString() {
        return "DeviceReply{" +
                "guestToken='" + guestToken + '\'' +
                '}';
    }
}
