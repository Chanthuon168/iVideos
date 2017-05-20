package com.hammercolab.theldpkh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hammercolab.theldpkh.ApiClient;
import com.hammercolab.theldpkh.ApiInterface;
import com.hammercolab.theldpkh.R;
import com.hammercolab.theldpkh.adapter.NewsAdapter;
import com.hammercolab.theldpkh.model.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chan Thuon on 4/27/2017.
 */
public class FragmentNews extends Fragment {
    private List<News> news = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private NewsAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private int sizeVideo;

    public FragmentNews() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        layoutManager = new LinearLayoutManager(getActivity());
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
                    adapter = new NewsAdapter(getActivity(), news);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    public void refreshData() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<News>> callComment = service.getNews();
        callComment.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                news = response.body();
                if (sizeVideo != news.size()) {
                    adapter = new NewsAdapter(getActivity(), news);
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
}