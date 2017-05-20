package com.hammercolab.theldpkh.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.hammercolab.theldpkh.ApiClient;
import com.hammercolab.theldpkh.model.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chan Thuon on 9/13/2016.
 */
public class ViewPagerNewsAdapter extends PagerAdapter {
    Context ssContext;
    private List<News> images = new ArrayList<>();
    int position;

    public ViewPagerNewsAdapter(Context ssContext, List<News> images, int position) {
        this.ssContext = ssContext;
        this.images = images;
        this.position = position;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View ssView, Object ssObject) {
        return ssView == ((ImageView) ssObject);
    }

    @Override
    public Object instantiateItem(ViewGroup ssContainer, int ssPosition) {
        ImageView ssImageView = new ImageView(ssContext);
        ssImageView.setPadding(0, 0, 0, 0);
        ssImageView.setAdjustViewBounds(true);
        Uri uri = Uri.parse(ApiClient.BASE_URL + images.get(ssPosition).getImage());
        ssContext = ssImageView.getContext();
        Picasso.with(ssContext).load(uri).into(ssImageView);
        Log.d("image", images.get(ssPosition).getImage());

        ((ViewPager) ssContainer).addView(ssImageView, 0);
        return ssImageView;
    }

    @Override
    public void destroyItem(ViewGroup ssContainer, int ssPosition,
                            Object ssObject) {
        ((ViewPager) ssContainer).removeView((ImageView) ssObject);
    }
}
