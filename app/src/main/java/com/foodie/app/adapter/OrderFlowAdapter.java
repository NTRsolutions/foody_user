package com.foodie.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.model.NotificationItem;
import com.foodie.app.model.OrderFlow;
import com.foodie.app.model.Restaurant;

import java.util.List;

import static com.foodie.app.helper.CommonClass.isSelectedOrder;

/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class OrderFlowAdapter extends RecyclerView.Adapter<OrderFlowAdapter.MyViewHolder> {
    private List<OrderFlow> list;
    private Context context;
    public  String orderStatus="";


    public OrderFlowAdapter(List<OrderFlow> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_flow_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public void add(OrderFlow item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(OrderFlow item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderFlow orderFlow = list.get(position);
        holder.statusTitle.setText(orderFlow.statusTitle);
        holder.statusDescription.setText(orderFlow.statusDescription);
        holder.statusImage.setImageResource(orderFlow.statusImage);
        if(orderFlow.status.contains(isSelectedOrder.getStatus())){
            holder.statusTitle.setTextColor(context.getResources().getColor(R.color.colorTextBlack));
        }else {
            holder.statusTitle.setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
        }

        if (list.size() == position + 1)
            holder.viewLine.setVisibility(View.GONE);
        else
            holder.viewLine.setVisibility(View.VISIBLE);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView statusImage;
        private View viewLine;
        private TextView statusTitle, statusDescription;


        private MyViewHolder(View view) {
            super(view);
            statusImage = (ImageView) view.findViewById(R.id.order_status_img);
            statusTitle = (TextView) view.findViewById(R.id.order_status_title);
            statusDescription = (TextView) view.findViewById(R.id.order_status_description);
            viewLine = (View) view.findViewById(R.id.view_line);
//            notificatioLayout.setOnClickListener(this);
        }

        public void onClick(View v) {
//            if (v.getId() == notificatioLayout.getId()) {
////                context.startActivity(new Intent(context, HotelViewActivity.class));
//                //Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
//            }
        }

    }


}