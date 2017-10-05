package com.foodie.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.foodie.app.R;
import com.foodie.app.activities.HotelViewActivity;
import com.foodie.app.fragments.HomeFragment;
import com.foodie.app.model.Restaurant;
import com.foodie.app.model.ShopsModel;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.MyViewHolder> {
    private List<ShopsModel> list;
    private Context context;
    private Activity activity;

    public RestaurantsAdapter(List<ShopsModel> list, Context con,Activity act) {
        this.list = list;
        this.context = con;
        this.activity = act;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public void add(ShopsModel item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(ShopsModel item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ShopsModel shops = list.get(position);

        Glide.with(context).load(shops.getAvatar()).placeholder(R.drawable.item1).dontAnimate()
                .error(R.drawable.item1).into(holder.dishImg);
        holder.restaurantName.setText(shops.getName());
        holder.category.setText(shops.getDescription());
        if(shops.getOfferPercent()==null){
            holder.offer.setVisibility(View.GONE);
        }else {
            holder.offer.setVisibility(View.VISIBLE);
            holder.offer.setText("Flat "+shops.getOfferPercent().toString()+"% offer on all Orders");
        }
//        if(shops.getav().equalsIgnoreCase("")){
//            holder.offer.setVisibility(View.GONE);
//            holder.restaurantInfo.setVisibility(View.GONE);
//
//        }else {
//            holder.restaurantInfo.setVisibility(View.VISIBLE);
//            holder.restaurantInfo.setText(shops.getAvailability());
//        }

        if(shops.getRatings()!=null)
        holder.rating.setText(""+Double.parseDouble(shops.getRatings().getRating()));
        else
            holder.rating.setText("No Rating");
        holder.distanceTime.setText(shops.getEstimatedDeliveryTime().toString()+" Mins");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout itemView;
        private ImageView dishImg;
        private TextView restaurantName, category, offer, rating, restaurantInfo, price,distanceTime;


        private MyViewHolder(View view) {
            super(view);
            itemView = (LinearLayout) view.findViewById(R.id.item_view);
            dishImg = (ImageView) view.findViewById(R.id.dish_img);
            restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
            category = (TextView) view.findViewById(R.id.category);
            offer = (TextView) view.findViewById(R.id.offer);
            rating = (TextView) view.findViewById(R.id.rating);
            restaurantInfo = (TextView) view.findViewById(R.id.restaurant_info);
            distanceTime = (TextView) view.findViewById(R.id.distance_time);
            price = (TextView) view.findViewById(R.id.price);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (v.getId() == itemView.getId()) {
                context.startActivity(new Intent(context, HotelViewActivity.class).putExtra("position",getAdapterPosition()));
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
                list.get(getAdapterPosition()).getCuisines();

            }
        }

    }


}
