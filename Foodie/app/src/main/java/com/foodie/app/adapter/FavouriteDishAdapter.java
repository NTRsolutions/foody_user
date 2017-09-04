package com.foodie.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.model.FavouriteDish;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by santhosh@appoets.com on 24-08-2017.
 */

public class FavouriteDishAdapter extends BaseAdapter {

    private Context context_;
    private List<FavouriteDish> list;

    public FavouriteDishAdapter(Context context, List<FavouriteDish> list) {
        this.context_ = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        FavouriteDish obj = list.get(position);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater mInflater = (LayoutInflater) context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.favorite_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.dishNameTxt.setText(obj.getName());
        holder.dishCategoryTxt.setText(obj.getCategory());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.dish_img)
        ImageView dishImg;
        @BindView(R.id.dish_name)
        TextView dishNameTxt;
        @BindView(R.id.dish_category)
        TextView dishCategoryTxt;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
