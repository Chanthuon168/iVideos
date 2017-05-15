package com.hammersmith.ivideos;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hammersmith.ivideos.adapter.ViewPagerAdapter;
import com.hammersmith.ivideos.adapter.ViewPagerNewsAdapter;
import com.hammersmith.ivideos.model.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ViewPagerNewsAdapter mCustomPagerAdapter;
    private List<News> albums = new ArrayList<>();
    private int id, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mViewPager = (ViewPager) findViewById(R.id.myviewpager);
        id = getIntent().getIntExtra("id", 0);
        position = getIntent().getIntExtra("position", 0);
        ApiInterface serviceAlbum = ApiClient.getClient().create(ApiInterface.class);
        Call<List<News>> callAlbum = serviceAlbum.getNews();
        callAlbum.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                albums = response.body();
                mCustomPagerAdapter = new ViewPagerNewsAdapter(ImageActivity.this, albums, position);
                mViewPager.setAdapter(mCustomPagerAdapter);
                mViewPager.setCurrentItem(position);
                mCustomPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {

            }
        });
    }
}
