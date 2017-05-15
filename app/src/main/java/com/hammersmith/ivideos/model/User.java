package com.hammersmith.ivideos.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tuanhai on 11/3/15.
 */
public class User {
    @SerializedName("token")
    private String deviceToken;

    public User() {
    }

    public User(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
