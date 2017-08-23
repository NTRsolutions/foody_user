package com.foodie.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.foodie.app.R;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by CSS22 on 23-08-2017.
 */

public class ProfileSettingsAdapter extends BaseAdapter {


    private static final String LOG_TAG = ProfileSettingsAdapter.class.getSimpleName();

    private Context context_;
    private List<String> items;
    List<Integer> listIcon;

    public ProfileSettingsAdapter(Context context, List<String> items, List<Integer> listIcon) {
            this.context_ = context;
            this.items = items;
            this.listIcon = listIcon;
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
                        context_.getSystemService(context_.LAYOUT_INFLATER_SERVICE);

                convertView = mInflater.inflate(R.layout.profile_settings_list_item, null);
            }

            ImageView setting_icon = (ImageView) convertView.findViewById(R.id.setting_icon);
            TextView tv = (TextView) convertView.findViewById(R.id.setting_label);

            setting_icon.setImageResource(listIcon.get(position));
            tv.setText(items.get(position));

            return convertView;
        }
}