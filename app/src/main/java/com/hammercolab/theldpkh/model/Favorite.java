package com.hammercolab.theldpkh.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan Thuon on 5/16/2017.
 */
public class Favorite {
    @SerializedName("id")
    private int id;
    @SerializedName("msg")
    private String msg;
    @SerializedName("vid_id")
    private int vidId;
    @SerializedName("token")
    private String token;
    @SerializedName("user_session")
    private String session;

    public Favorite() {
    }

    public Favorite(String session, int vidId) {
        this.session = session;
        this.vidId = vidId;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getVidId() {
        return vidId;
    }

    public void setVidId(int vidId) {
        this.vidId = vidId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
