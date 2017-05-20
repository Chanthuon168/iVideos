package com.hammercolab.theldpkh.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tuanhai on 11/3/15.
 */
public class User {
    @SerializedName("token")
    private String deviceToken;
    @SerializedName("notification")
    private String status;
    @SerializedName("user_session")
    private String session;
    @SerializedName("msg")
    private String msg;

    public User() {
    }

    public User(String session) {
        this.session = session;
    }

    public User(String session, String deviceToken) {
        this.session = session;
        this.deviceToken = deviceToken;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotification() {
        return status;
    }

    public void setNotification(String notification) {
        this.status = notification;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
