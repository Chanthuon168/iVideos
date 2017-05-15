package com.hammersmith.ivideos.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hammersmith.ivideos.ActivityPlay;
import com.hammersmith.ivideos.R;
import com.hammersmith.ivideos.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Chan Thuon on 3/8/2017.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder>{
    private Context context;
    private List<Video> videos;
    private Activity activity;
    private Typeface custom_font;

    public VideoAdapter(Activity activity, List<Video> videos){
        this.activity = activity;
        this.videos = videos;
        custom_font = Typeface.createFromAsset(activity.getAssets(), "fonts/ASENINE_.ttf");
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

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bookmark.setImageResource(R.drawable.bookmark);
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(activity, ActivityPlay.class));
                intent.putExtra("code",videos.get(position).getCode());
                intent.putExtra("title",videos.get(position).getTitle());
                intent.putExtra("viewCount", videos.get(position).getViewCount());
                intent.putExtra("likeCount", videos.get(position).getLikeCount());
                intent.putExtra("dislikeCount", videos.get(position).getDislikeCount());
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
        TextView title;
        LinearLayout layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            title = (TextView) itemView.findViewById(R.id.title);
            bookmark = (ImageView) itemView.findViewById(R.id.bookmark);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }
}
