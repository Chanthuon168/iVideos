package com.hammercolab.theldpkh;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chan Thuon on 9/9/2016.
 */
public class ApiClient {
    public static final String BASE_URL = "http://hammercolab.com/CartoonApi/ldp/";
    public static final String URL = "http://hammercolab.com/CartoonApi/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
