package com.hammercolab.theldpkh;

import android.icu.lang.UScript;

import com.hammercolab.theldpkh.model.Favorite;
import com.hammercolab.theldpkh.model.News;
import com.hammercolab.theldpkh.model.Setting;
import com.hammercolab.theldpkh.model.User;
import com.hammercolab.theldpkh.model.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Chan Thuon on 9/9/2016.
 */
public interface ApiInterface {
    @POST("ldp/video")
    Call<List<Video>> getAllVideo(@Body Video video, @Query("page") int pageNumber);

    @POST("ldp/video/category")
    Call<List<Video>> getVideo(@Body Video video, @Query("page") int pageNumber);

    @GET("ldp/news")
    Call<List<News>> getNews();

    @POST("ldp/video/save")
    Call<Video> postVideo(@Body Video video);

    @POST("ldp/create/devicetoken")
    Call<User> createDeviceToken(@Body User user);

    @POST("ldp/create/favorite")
    Call<Favorite> createFavorite(@Body Favorite favorite);

    @POST("ldp/video/favorite")
    Call<List<Video>> getVideoFavorite(@Body Video video, @Query("page") int pageNumber);

    @POST("ldp/delete/favorite")
    Call<List<Video>> deleteFavorite(@Body Favorite favorite);

    @POST("ldp/upload/image")
    Call<News> uploadFile (@Body News news);

    @POST("ldp/notification")
    Call<Setting> offNotification (@Body Setting setting);

    @POST("ldp/get/notification")
    Call<User> getNotification (@Body User user);

    @POST("ldp/generate/session")
    Call<User> generateSession (@Body User user);

    @POST("search")
    Call<List<Video>> getSearch(@Body Video video);

}
