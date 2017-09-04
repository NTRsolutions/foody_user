package com.foodie.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.foodie.app.R;
import com.foodie.app.model.FilterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santhosh@appoets.com on 28-08-2017.
 */

public class FilterAdapter extends SectionedRecyclerViewAdapter<FilterAdapter.ViewHolder> {

    private List<FilterModel> list = new ArrayList<>();
    private LayoutInflater inflater;

    public FilterAdapter(Context context, List<FilterModel> list) {
        Context context1 = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                v = inflater.inflate(R.layout.header_filter, parent, false);
                return new ViewHolder(v, true);
            case VIEW_TYPE_ITEM:
                v = inflater.inflate(R.layout.filter_list_item, parent, false);
                return new ViewHolder(v, false);
            default:
                v = inflater.inflate(R.layout.filter_list_item, parent, false);
                return new ViewHolder(v, false);
        }
    }

    @Override
    public int getSectionCount() {
        return list.size();
    }


    @Override
    public int getItemCount(int section) {
        return list.get(section).getFilters().size();
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
    public void onBindViewHolder(final ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        final String item = list.get(section).getFilters().get(relativePosition);
        holder.filterNameTxt.setText(item);
        holder.filterNameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.chkSelected.isChecked()){
                    holder.chkSelected.setChecked(false);
                }else {
                    holder.chkSelected.setChecked(true);
                }
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView headerTxt;
        TextView filterNameTxt;
        CheckBox chkSelected;
        LinearLayout itemLayout;

        public ViewHolder(View itemView, boolean isHeader) {
            super(itemView);
            if (isHeader) {
                headerTxt = (TextView) itemView.findViewById(R.id.header);
            } else {
                itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
                filterNameTxt = (TextView) itemView.findViewById(R.id.filter_name);
                chkSelected = (CheckBox) itemView.findViewById(R.id.chk_selected);

            }

        }

    }
}
