package com.hammercolab.theldpkh.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan Thuon on 3/8/2017.
 */
public class Video {
    @SerializedName("id")
    private int id;
    @SerializedName("cat_id")
    private int catId;
    @SerializedName("code")
    private String code;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("title")
    private String title;
    @SerializedName("viewCount")
    private String viewCount;
    @SerializedName("likeCount")
    private String likeCount;
    @SerializedName("dislikeCount")
    private String dislikeCount;
    @SerializedName("msg")
    private String msg;
    @SerializedName("favorite")
    private String favorite;
    @SerializedName("token")
    private String token;
    @SerializedName("user_session")
    private String session;
    @SerializedName("keyword")
    private String key;

    public Video() {
    }

    public Video(int catId, String session) {
        this.catId = catId;
        this.session = session;
    }

    public Video(String key, String session) {
        this.key = key;
        this.session = session;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Video(String code, int catId) {
        this.code = code;
        this.catId = catId;
    }

    public Video(String session) {
        this.session = session;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(String dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
}
