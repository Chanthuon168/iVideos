package com.hammercolab.theldpkh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hammercolab.theldpkh.ApiClient;
import com.hammercolab.theldpkh.ApiInterface;
import com.hammercolab.theldpkh.EndlessRecyclerOnScrollListener;
import com.hammercolab.theldpkh.PrefUtils;
import com.hammercolab.theldpkh.R;
import com.hammercolab.theldpkh.adapter.FavoriteAdapter;
import com.hammercolab.theldpkh.model.User;
import com.hammercolab.theldpkh.model.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chan Thuon on 4/27/2017.
 */
public class FragmentFavorite extends Fragment {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private FavoriteAdapter adapter;
    private LinearLayout lProgress;
    private User user;
    private Video video;
    int videoPage = 1;
    List<Video> videoList;

    public FragmentFavorite() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        user = PrefUtils.getCurrentUser(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        lProgress = (LinearLayout) view.findViewById(R.id.progress);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setNestedScrollingEnabled(false);
        videoList = new ArrayList<>();
        getVideoFavorite(videoPage);
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FavoriteAdapter(getActivity(), videoList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                videoPage++;
                getVideoFavorite(page);
            }
        });
    }

    private void getVideoFavorite(final int page) {

        if (PrefUtils.getCurrentUser(getActivity()) != null) {
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
    }
}
