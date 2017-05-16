package com.hammersmith.ivideos;

import com.hammersmith.ivideos.model.Favorite;
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
    @POST("video")
    Call<List<Video>> getAllVideo(@Body Video video);

    @POST("video/category")
    Call<List<Video>> getVideo(@Body Video video);

    @GET("news")
    Call<List<News>> getNews();

    @POST("save_video")
    Call<Video> postVideo(@Body Video video);

    @POST("create/deviceToken")
    Call<User> createDeviceToken(@Body User user);

    @POST("create/favorite")
    Call<Favorite> createFavorite(@Body Favorite favorite);

    @POST("video/favorite")
    Call<List<Video>> getVideoFavorite(@Body Video video);

    @POST("delete/favorite")
    Call<List<Video>> deleteFavorite(@Body Favorite favorite);

}
