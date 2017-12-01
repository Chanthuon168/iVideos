package com.hammercolab.theldpkh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.hammercolab.theldpkh.adapter.VideoPlayAdapter;
import com.hammercolab.theldpkh.model.Favorite;
import com.hammercolab.theldpkh.model.User;
import com.hammercolab.theldpkh.model.Video;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPlay extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;
    private YouTubePlayer mPlayer;

    private List<Video> videos = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private VideoPlayAdapter adapter;
    private String title, code, viewCount, likeCount, dislikeCount, favorite;
    private TextView txtTitle, txtViewCount, txtLikeCount, txtDislikeCount;
    private LinearLayout lProgress;
    private int sizeVideo;
    private ImageView bookmark;
    private Favorite mFavorite;
    private User user;
    private int id;
    private Video video;
    int videoPage = 1;
    List<Video> videoList;
    private LinearLayout mainLayout;

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            id = intent.getIntExtra("id", 0);
            code = intent.getStringExtra("code");
            title = intent.getStringExtra("title");
            viewCount = intent.getStringExtra("viewCount");
            likeCount = intent.getStringExtra("likeCount");
            dislikeCount = intent.getStringExtra("dislikeCount");
            favorite = intent.getStringExtra("favorite");

            if (favorite.equals("checked")) {
                bookmark.setImageResource(R.drawable.cloud_check);
            } else {
                bookmark.setImageResource(R.drawable.cloud_download);
            }

            String views = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(viewCount));
            String likes = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(likeCount));
            String dislikes = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(dislikeCount));

            txtTitle.setText(title);
            txtViewCount.setText(views + " views");
            txtLikeCount.setText(likes);
            txtDislikeCount.setText(dislikes);

//            if (mPlayer != null) {
//                mPlayer.loadVideo(code);
//                mPlayer.play();
//            }

            if (mPlayer != null) {
                try {
                    mPlayer.loadVideo(code);
                    mPlayer.play();
                } catch (IllegalStateException e) {

                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_activity_play);
        user = PrefUtils.getCurrentUser(ActivityPlay.this);
        lProgress = (LinearLayout) findViewById(R.id.progress);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        txtTitle = (TextView) findViewById(R.id.title);
        txtViewCount = (TextView) findViewById(R.id.viewCount);
        txtLikeCount = (TextView) findViewById(R.id.likeCount);
        txtDislikeCount = (TextView) findViewById(R.id.dislikeCount);
        findViewById(R.id.share).setOnClickListener(this);
        bookmark = (ImageView) findViewById(R.id.bookmark);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        bookmark.setOnClickListener(this);
        videoList = new ArrayList<>();

        id = getIntent().getIntExtra("id", 0);
        code = getIntent().getStringExtra("code");
        title = getIntent().getStringExtra("title");
        viewCount = getIntent().getStringExtra("viewCount");
        likeCount = getIntent().getStringExtra("likeCount");
        dislikeCount = getIntent().getStringExtra("dislikeCount");
        favorite = getIntent().getStringExtra("favorite");

        if (favorite.equals("checked")) {
            bookmark.setImageResource(R.drawable.cloud_check);
        } else {
            bookmark.setImageResource(R.drawable.cloud_download);
        }

        String views = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(viewCount));
        String likes = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(likeCount));
        String dislikes = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(dislikeCount));

        txtTitle.setText(title);
        txtViewCount.setText(views + " views");
        txtLikeCount.setText(likes);
        txtDislikeCount.setText(dislikes);

        // Initializing video player with developer key
        youTubeView.initialize(Config.DEVELOPER_KEY, this);

        getVideoAll(videoPage);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new VideoPlayAdapter(ActivityPlay.this, videoList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                videoPage++;
                getVideoAll(page);
            }
        });


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message"));
    }

    private void getVideoAll(final int page) {

        video = new Video(user.getSession());
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Video>> callComment = service.getAllVideo(video, page);
        callComment.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                videos = response.body();
                sizeVideo = videos.size();
                lProgress.setVisibility(View.GONE);
                if (videos.size() > 0) {
                    videoList.addAll(response.body());
                    adapter.notifyItemRangeInserted(adapter.getItemCount(), videoList.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                lProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {

        this.mPlayer = player;

        if (!wasRestored) {
            player.setPlayerStyle(PlayerStyle.DEFAULT);
            player.loadVideo(code);
            mPlayer = player;
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                i.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + code);
                startActivity(Intent.createChooser(i, "Share with"));
                break;
            case R.id.bookmark:
                mFavorite = new Favorite(user.getSession(), id);
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<Favorite> favoriteCall = service.createFavorite(mFavorite);
                favoriteCall.enqueue(new Callback<Favorite>() {
                    @Override
                    public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                        mFavorite = response.body();
                        if (mFavorite.getMsg().equals("checked")) {
                            bookmark.setImageResource(R.drawable.cloud_check);
                            Snackbar snackbar = Snackbar
                                    .make(mainLayout, "Video was saved on favorite", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            bookmark.setImageResource(R.drawable.cloud_download);
                            Snackbar snackbar = Snackbar
                                    .make(mainLayout, "Video was removed from favorite", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Favorite> call, Throwable t) {

                    }
                });
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mPlayer != null) {
            mPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
