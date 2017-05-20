package com.hammercolab.theldpkh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.hammercolab.theldpkh.adapter.NewsAdapter;
import com.hammercolab.theldpkh.model.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {
    private List<News> news = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private NewsAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private int sizeVideo;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Announcement");
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
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<News>> callComment = service.getNews();
        callComment.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                news = response.body();
                sizeVideo = news.size();
                swipeRefresh.setRefreshing(false);
                if (news.size() > 0) {
                    adapter = new NewsAdapter(NewsActivity.this, news);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    public void refreshData() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<News>> callComment = service.getNews();
        callComment.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                news = response.body();
                if (sizeVideo != news.size()) {
                    adapter = new NewsAdapter(NewsActivity.this, news);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    sizeVideo = news.size();
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }
}
