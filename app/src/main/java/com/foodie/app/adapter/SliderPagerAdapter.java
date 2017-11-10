package com.foodie.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.foodie.app.R;
import com.foodie.app.models.Image;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 09-11-2017.
 */

public class SliderPagerAdapter extends PagerAdapter {
    private Activity activity;
    private List<Image> image_arraylist;

    public SliderPagerAdapter(Activity activity, List<Image> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
        ImageView im_slider = view.findViewById(R.id.im_slider);
        Glide.with(activity.getApplicationContext()).load(image_arraylist.get(position).getUrl()).placeholder(R.drawable.ic_restaurant_place_holder).into(im_slider);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
