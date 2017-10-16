package com.foodie.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.activities.OtherHelpActivity;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 23-08-2017.
 */

public class HelpListAdapter extends BaseAdapter {

    private static final String LOG_TAG = HelpListAdapter.class.getSimpleName();
    private Context context_;
    private List<String> items;

    public HelpListAdapter(Context context, List<String> items) {
        this.context_ = context;
        this.items = items;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.help_list_item, null);
        }

        final TextView tv = (TextView) convertView.findViewById(R.id.setting_label);
        tv.setText(items.get(position));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context_.startActivity(new Intent(context_, OtherHelpActivity.class).putExtra("type", tv.getText().toString()));
            }
        });

        return convertView;
    }
}