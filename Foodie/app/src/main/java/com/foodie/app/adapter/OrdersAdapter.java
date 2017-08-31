package com.foodie.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.foodie.app.R;
import com.foodie.app.model.Order;
import com.foodie.app.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santhosh@appoets.com on 28-08-2017.
 */

public class OrdersAdapter extends SectionedRecyclerViewAdapter<OrdersAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<OrderModel> list = new ArrayList<>();

    public OrdersAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                v = inflater.inflate(R.layout.header_order, parent, false);
                return new ViewHolder(v, true);
            case VIEW_TYPE_ITEM:
                v = inflater.inflate(R.layout.orders_list_item, parent, false);
                return new ViewHolder(v, false);
            default:
                v = inflater.inflate(R.layout.orders_list_item, parent, false);
                return new ViewHolder(v, false);
        }
    }

    @Override
    public int getSectionCount() {
        return list.size();
    }


    @Override
    public int getItemCount(int section) {
        return list.get(section).getOrders().size();
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, final int section) {
        holder.headerTxt.setText(list.get(section).getHeader());
        holder.headerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(list.get(section).getHeader());
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        final Order object = list.get(section).getOrders().get(relativePosition);
        holder.restaurantNameTxt.setText(object.restaurantName);
        holder.restaurantAddressTxt.setText(object.restaurantAddress);
        holder.dishNameTxt.setText(object.dishName);
        holder.dateTimeTxt.setText(object.dateTime);

        holder.reorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(object.dishName);
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView headerTxt;
        TextView restaurantNameTxt, restaurantAddressTxt, dishNameTxt, dateTimeTxt;
        Button reorderBtn;
        LinearLayout itemLayout;

        public ViewHolder(View itemView, boolean isHeader) {
            super(itemView);
            if (isHeader) {
                headerTxt = (TextView) itemView.findViewById(R.id.header);
            } else {
                itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
                restaurantNameTxt = (TextView) itemView.findViewById(R.id.restaurant_name);
                restaurantAddressTxt = (TextView) itemView.findViewById(R.id.restaurant_address);
                dishNameTxt = (TextView) itemView.findViewById(R.id.dish_name);
                dateTimeTxt = (TextView) itemView.findViewById(R.id.date_time);
                reorderBtn = (Button) itemView.findViewById(R.id.reorder);
            }

        }

    }
}