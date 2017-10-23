package com.foodie.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.activities.OtherHelpActivity;
import com.foodie.app.model.DisputeMessage;
import com.foodie.app.model.NotificationItem;
import com.foodie.app.model.Restaurant;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class DisputeMessageAdapter extends RecyclerView.Adapter<DisputeMessageAdapter.MyViewHolder> {
    private List<DisputeMessage> list;
    private Context context;

    public DisputeMessageAdapter(List<DisputeMessage> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dispute_message_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public void add(DisputeMessage item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(DisputeMessage item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DisputeMessage disputeMessage = list.get(position);
        holder.diputeMessageTxt.setText(disputeMessage.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout rootLayout;
        private TextView diputeMessageTxt;


        private MyViewHolder(View view) {
            super(view);
            rootLayout = (LinearLayout) view.findViewById(R.id.root_layout);
            diputeMessageTxt = (TextView) view.findViewById(R.id.dispute_message);
            rootLayout.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (v.getId() == rootLayout.getId()) {
         context.startActivity(new Intent(context, OtherHelpActivity.class).putExtra("type", diputeMessageTxt.getText().toString()));
            }
        }

    }


}
