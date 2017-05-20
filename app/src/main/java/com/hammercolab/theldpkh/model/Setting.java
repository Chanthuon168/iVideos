package com.hammercolab.theldpkh.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan Thuon on 5/19/2017.
 */
public class Setting {

    @SerializedName("notification")
    private String status;
    @SerializedName("user_session")
    private String session;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Setting() {
    }

    public Setting(String session, String status) {
        this.session = session;
        this.status = status;
    }
}
