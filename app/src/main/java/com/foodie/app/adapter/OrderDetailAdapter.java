package com.foodie.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.activities.HotelViewActivity;
import com.foodie.app.model.OrderItem;
import com.foodie.app.model.Restaurant;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
    private List<OrderItem> list;
    private Context context;

    public OrderDetailAdapter(List<OrderItem> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public void add(OrderItem item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Restaurant item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderItem orderItem = list.get(position);
        holder.dishName.setText(orderItem.name);
        holder.price.setText(orderItem.price);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout itemView;
        private ImageView dishImg;
        private TextView dishName, price;

        private MyViewHolder(View view) {
            super(view);
            itemView = (LinearLayout) view.findViewById(R.id.item_view);
            dishName = (TextView) view.findViewById(R.id.restaurant_name);
            price = (TextView) view.findViewById(R.id.price);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (v.getId() == itemView.getId()) {
//                context.startActivity(new Intent(context, HotelViewActivity.class));
                //Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        }

    }


}