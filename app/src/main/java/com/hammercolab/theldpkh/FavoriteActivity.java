package com.hammercolab.theldpkh;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hammercolab.theldpkh.adapter.FavoriteAdapter;
import com.hammercolab.theldpkh.model.User;
import com.hammercolab.theldpkh.model.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private List<Video> videos = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private FavoriteAdapter adapter;
    private User user;
    private Video video;
    private String title;
    int videoPage = 1;
    List<Video> videoList;
    private LinearLayout lProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        user = PrefUtils.getCurrentUser(FavoriteActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        videoList = new ArrayList<>();
        title = getIntent().getStringExtra("title");

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        lProgress = (LinearLayout) findViewById(R.id.progress);
        layoutManager = new GridLayoutManager(FavoriteActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FavoriteAdapter(FavoriteActivity.this, videoList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                videoPage++;
                getVideoFavorite(page);
            }
        });

        getVideoFavorite(videoPage);

    }

    private void getVideoFavorite(final int page) {
        video = new Video(user.getSession());
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Video>> callComment = service.getVideoFavorite(video, page);
        callComment.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                lProgress.setVisibility(View.GONE);
                videoList.addAll(response.body());
                adapter.notifyItemRangeInserted(adapter.getItemCount(), videoList.size() - 1);
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                lProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }
}
