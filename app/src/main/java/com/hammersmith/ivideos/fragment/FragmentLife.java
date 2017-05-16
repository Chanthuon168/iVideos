package com.hammersmith.ivideos.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hammersmith.ivideos.ApiClient;
import com.hammersmith.ivideos.ApiInterface;
import com.hammersmith.ivideos.PrefUtils;
import com.hammersmith.ivideos.R;
import com.hammersmith.ivideos.adapter.VideoAdapter;
import com.hammersmith.ivideos.model.User;
import com.hammersmith.ivideos.model.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chan Thuon on 4/27/2017.
 */
public class FragmentLife extends Fragment {
    private List<Video> videos = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private VideoAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private int sizeVideo;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private User user;
    private Video video;

    public FragmentLife() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        user = PrefUtils.getCurrentUser(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        layoutManager = new GridLayoutManager(getActivity(), 2);
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

        if (PrefUtils.getCurrentUser(getActivity()) != null) {
            video = new Video(4, user.getDeviceToken());
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<List<Video>> callComment = service.getVideo(video);
            callComment.enqueue(new Callback<List<Video>>() {
                @Override
                public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                    videos = response.body();
                    sizeVideo = videos.size();
                    swipeRefresh.setRefreshing(false);
                    if (videos.size() > 0) {
                        adapter = new VideoAdapter(getActivity(), videos);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<Video>> call, Throwable t) {
                    swipeRefresh.setRefreshing(false);
                }
            });
        }


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
            }
        });

        return view;
    }

    public void refreshData() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Video>> callComment = service.getVideo(video);
        callComment.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                videos = response.body();
                if (sizeVideo != videos.size()) {
                    adapter = new VideoAdapter(getActivity(), videos);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    sizeVideo = videos.size();
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}
