package com.hammersmith.ivideos;

import com.hammersmith.ivideos.model.News;
import com.hammersmith.ivideos.model.User;
import com.hammersmith.ivideos.model.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Chan Thuon on 9/9/2016.
 */
public interface ApiInterface {
    @GET("video")
    Call<List<Video>> getAllVideo();

    @GET("video/{id}")
    Call<List<Video>> getVideo(@Path("id") int id);

    @GET("news")
    Call<List<News>> getNews();

    @POST("save_video")
    Call<Video> postVideo(@Body Video video);

    @POST("create/deviceToken")
    Call<User> createDeviceToken(@Body User user);
}
