package com.foodie.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.foodie.app.R;
import com.foodie.app.model.Location;
import com.foodie.app.model.LocationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santhosh@appoets.com on 28-08-2017.
 */

public class DeliveryLocationAdapter extends SectionedRecyclerViewAdapter<DeliveryLocationAdapter.ViewHolder> {

    List<LocationModel> list = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public DeliveryLocationAdapter(Context context, List<LocationModel> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                v = inflater.inflate(R.layout.header, parent, false);
                return new ViewHolder(v, true);
            case VIEW_TYPE_ITEM:
                v = inflater.inflate(R.layout.location_list_item, parent, false);
                return new ViewHolder(v, false);
            default:
                v = inflater.inflate(R.layout.location_list_item, parent, false);
                return new ViewHolder(v, false);
        }
    }

    @Override
    public int getSectionCount() {
        return list.size();
    }


    @Override
    public int getItemCount(int section) {
        return list.get(section).getLocations().size();
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, final int section) {
        holder.path.setText(list.get(section).getHeader());
        holder.path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(list.get(section).getHeader());
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        final Location object = list.get(section).getLocations().get(relativePosition);
        holder.addressLabel.setText(object.name);
        holder.address.setText(object.address);

        setIcon(holder.icon, object.icon_id);

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(object.name);

            }
        });

    }


    public void setIcon(ImageView imgView, Integer id) {
        switch (id) {
            case 1:
                imgView.setImageResource(R.drawable.home);
                break;
            case 2:
                imgView.setImageResource(R.drawable.ic_work);
                break;
            default:
                imgView.setImageResource(R.drawable.ic_map_marker);
                break;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView path;
        TextView addressLabel;
        TextView address;
        ImageView icon;
        LinearLayout itemLayout;

        public ViewHolder(View itemView, boolean isHeader) {
            super(itemView);
            if (isHeader) {
                path = (TextView) itemView.findViewById(R.id.header);
            } else {
                itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
                addressLabel = (TextView) itemView.findViewById(R.id.address_label);
                address = (TextView) itemView.findViewById(R.id.address);
                icon = (ImageView) itemView.findViewById(R.id.icon);
            }

        }


    }
}
