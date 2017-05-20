package com.hammercolab.theldpkh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hammercolab.theldpkh.ActivityPlay;
import com.hammercolab.theldpkh.ApiClient;
import com.hammercolab.theldpkh.ApiInterface;
import com.hammercolab.theldpkh.PrefUtils;
import com.hammercolab.theldpkh.R;
import com.hammercolab.theldpkh.model.Favorite;
import com.hammercolab.theldpkh.model.User;
import com.hammercolab.theldpkh.model.Video;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chan Thuon on 3/8/2017.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>{
    private Context context;
    private List<Video> videos;
    private Activity activity;
    private Favorite favorite;
    private User user;

    public FavoriteAdapter(Activity activity, List<Video> videos){
        this.activity = activity;
        this.videos = videos;
        user = PrefUtils.getCurrentUser(activity);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_itm, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Uri uri = Uri.parse(videos.get(position).getThumbnail());
        context = holder.imageView.getContext();
        Picasso.with(context).load(uri).into(holder.imageView);
        holder.title.setText(videos.get(position).getTitle());
        String viewCount = videos.get(position).getViewCount();
        if (!viewCount.equals("")) {
            String views = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(viewCount));
            holder.views.setText(views + " views");
        }

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite = new Favorite(user.getSession(), videos.get(position).getId());
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                final Call<Favorite> favoriteCall = service.createFavorite(favorite);
                favoriteCall.enqueue(new Callback<Favorite>() {
                    @Override
                    public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                        favorite = response.body();
                        if (favorite.getMsg().equals("checked")) {
                            holder.bookmark.setImageResource(R.drawable.bookmarked);
                            favorite = new Favorite(user.getSession(), videos.get(position).getId());
                            ApiInterface mService = ApiClient.getClient().create(ApiInterface.class);
                            Call<List<Video>> mCall = mService.deleteFavorite(favorite);
                            mCall.enqueue(new Callback<List<Video>>() {
                                @Override
                                public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                                    videos = response.body();
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<List<Video>> call, Throwable t) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Favorite> call, Throwable t) {

                    }
                });
            }
        });
        if (videos.get(position).getFavorite().equals("checked")) {
            holder.bookmark.setImageResource(R.drawable.bookmarked);
        } else {
            holder.bookmark.setImageResource(R.drawable.bookmark);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(activity, ActivityPlay.class));
                intent.putExtra("id", videos.get(position).getId());
                intent.putExtra("code",videos.get(position).getCode());
                intent.putExtra("title",videos.get(position).getTitle());
                intent.putExtra("viewCount", videos.get(position).getViewCount());
                intent.putExtra("likeCount", videos.get(position).getLikeCount());
                intent.putExtra("dislikeCount", videos.get(position).getDislikeCount());
                intent.putExtra("favorite", videos.get(position).getFavorite());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, bookmark;
        TextView title, views;
        LinearLayout layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            title = (TextView) itemView.findViewById(R.id.title);
            bookmark = (ImageView) itemView.findViewById(R.id.bookmark);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            views = (TextView) itemView.findViewById(R.id.views);
        }
    }
}
