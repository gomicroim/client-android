package com.gomicroim.lib.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceReq implements Serializable {
    @SerializedName("device_id")
    private String deviceId;

    @SerializedName("manufacturer")
    private String manufacturer;

    @SerializedName("model")
    private String model;

    @SerializedName("serial")
    private String serial;

    @SerializedName("platform")
    private String platform;

    @SerializedName("os_version")
    private String osVersion;

    @SerializedName("resolution")
    private String resolution;

    @SerializedName("language")
    private String language;

    @SerializedName("cpu_architecture")
    private String cpuArchitecture;

    public DeviceReq(String deviceId, String platform) {
        this.deviceId = deviceId;
        this.platform = platform;
    }
}
