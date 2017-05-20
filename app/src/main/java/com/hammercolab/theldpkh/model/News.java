package com.hammercolab.theldpkh.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan Thuon on 3/8/2017.
 */
public class News {
    @SerializedName("id")
    private int id;
    @SerializedName("image")
    private String image;
    @SerializedName("msg")
    private String msg;

    public News() {
    }

    public News(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
