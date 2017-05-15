package com.hammersmith.ivideos.adapter;

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

import com.hammersmith.ivideos.ActivityPlay;
import com.hammersmith.ivideos.ApiClient;
import com.hammersmith.ivideos.ApiInterface;
import com.hammersmith.ivideos.ImageActivity;
import com.hammersmith.ivideos.R;
import com.hammersmith.ivideos.model.News;
import com.hammersmith.ivideos.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Chan Thuon on 3/8/2017.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private Context context;
    private List<News> news;
    private Activity activity;

    public NewsAdapter(Activity activity, List<News> news) {
        this.activity = activity;
        this.news = news;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_news_itm, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Uri uri = Uri.parse(ApiClient.BASE_URL + news.get(position).getImage());
        context = holder.imageView.getContext();
        Picasso.with(context).load(uri).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ImageActivity.class);
                intent.putExtra("position",position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }
}
