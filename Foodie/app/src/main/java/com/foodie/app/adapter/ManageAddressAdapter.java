package com.foodie.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.model.Location;

import java.util.List;

/**
 * Created by CSS22 on 22-08-2017.
 */

public class ManageAddressAdapter extends RecyclerView.Adapter<ManageAddressAdapter.MyViewHolder> {

    public List<Location> list;
    public Context context;

    public ManageAddressAdapter(List<Location> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Location location = list.get(position);

        holder.addressLabelTxt.setText(location.name);
        holder.addressTxt.setText(location.address);
        setIcon(holder.iconImg, location.icon_id);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setIcon(ImageView imgView, Integer id) {
        switch (id) {
            case 1:
                imgView.setImageResource(R.drawable.ic_home);
                break;
            case 2:
                imgView.setImageResource(R.drawable.ic_work);
                break;
            default:
                imgView.setImageResource(R.drawable.ic_map_marker);
                break;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView addressLabelTxt, addressTxt;
        private Button editBtn, deleteBtn;
        private ImageView iconImg;

        private MyViewHolder(View view) {
            super(view);
            addressLabelTxt = (TextView) view.findViewById(R.id.address_label);
            addressTxt = (TextView) view.findViewById(R.id.address);
            editBtn = (Button) view.findViewById(R.id.edit);
            deleteBtn = (Button) view.findViewById(R.id.delete);
            iconImg = (ImageView) view.findViewById(R.id.icon);
            //itemView.setOnClickListener( this);
            editBtn.setOnClickListener(this);
            deleteBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == editBtn.getId()) {
                Toast.makeText(v.getContext(), "editBtn PRESSED = " + list.get(position).name + list.get(position).address, Toast.LENGTH_SHORT).show();
            } else if (v.getId() == deleteBtn.getId()) {
                Toast.makeText(v.getContext(), "deleteBtn PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        }

    }

}
