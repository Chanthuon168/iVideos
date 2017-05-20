package com.hammercolab.theldpkh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hammercolab.theldpkh.R;
import com.hammercolab.theldpkh.model.Video;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Chan Thuon on 4/27/2017.
 */
public class VideoPlayAdapter extends RecyclerView.Adapter<VideoPlayAdapter.MyViewHolder> {
    private Context context;
    private List<Video> videos;
    private Activity activity;

    public VideoPlayAdapter(Activity activity, List<Video> videos) {
        this.activity = activity;
        this.videos = videos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_play, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Uri uri = Uri.parse(videos.get(position).getThumbnail());
        context = holder.imageView.getContext();
        Picasso.with(context).load(uri).into(holder.imageView);
        holder.title.setText(videos.get(position).getTitle());
//        String viewCount = videos.get(position).getViewCount();
//        if (!viewCount.equals("")) {
//            String views = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(viewCount));
//            holder.views.setText(views + " views");
//        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("custom-message");
                intent.putExtra("id", videos.get(position).getId());
                intent.putExtra("code", videos.get(position).getCode());
                intent.putExtra("title", videos.get(position).getTitle());
                intent.putExtra("viewCount", videos.get(position).getViewCount());
                intent.putExtra("likeCount", videos.get(position).getLikeCount());
                intent.putExtra("dislikeCount", videos.get(position).getDislikeCount());
                intent.putExtra("favorite", videos.get(position).getFavorite());
                LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, views;
        LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            title = (TextView) itemView.findViewById(R.id.title);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
//            views = (TextView) itemView.findViewById(R.id.views);
        }
    }
}

