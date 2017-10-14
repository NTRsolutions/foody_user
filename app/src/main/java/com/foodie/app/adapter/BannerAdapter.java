package com.foodie.app.adapter;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.foodie.app.R;
import com.foodie.app.activities.HotelViewActivity;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.model.Banner;
import com.foodie.app.model.ImpressiveDish;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.MyViewHolder> {
    private List<Banner> list;
    Context context;
    Activity activity;

    public BannerAdapter(List<Banner> list, Context con, Activity activity) {
        this.list = list;
        this.context = con;
        this.activity = activity;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.impressive_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Banner banner = list.get(position);
        Glide.with(context).load(banner.getUrl()).placeholder(R.drawable.item1).dontAnimate()
                .error(R.drawable.item1).into(holder.bannerImg);
        holder.bannerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Banner banner = list.get(position);
                context.startActivity(new Intent(context, HotelViewActivity.class));
                CommonClass.getInstance().selectedShop = banner.getShop();
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
                Log.d("Hello", "onItemClick position: " + banner.getShop().getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView bannerImg;


        public MyViewHolder(View view) {
            super(view);
            bannerImg = (ImageView) view.findViewById(R.id.banner_img);

        }


    }


}
