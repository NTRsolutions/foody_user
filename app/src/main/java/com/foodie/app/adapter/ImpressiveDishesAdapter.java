package com.foodie.app.adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import com.foodie.app.R;
import com.foodie.app.model.ImpressiveDish;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class ImpressiveDishesAdapter extends RecyclerView.Adapter<ImpressiveDishesAdapter.MyViewHolder> {
    private static ClickListener clickListener;
    private List<ImpressiveDish> list;

    public ImpressiveDishesAdapter(List<ImpressiveDish> list, Context con) {
        this.list = list;
        Context context = con;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ImpressiveDishesAdapter.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.impressive_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImpressiveDish movie = list.get(position);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView dishImg;


        public MyViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            dishImg = (ImageView) view.findViewById(R.id.dishImg);

        }

        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }


    }


}
