package com.foodie.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.model.Promotions;
import com.foodie.app.model.Restaurant;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class PromotionsAdapter extends RecyclerView.Adapter<PromotionsAdapter.MyViewHolder> {
    private List<Promotions> list;
    private Context context;

    public PromotionsAdapter(List<Promotions> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promotions_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public void add(Promotions item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Restaurant item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Promotions PromotionsModel = list.get(position);
        holder.promotionsDate.setText(PromotionsModel.promotionsDate);
        holder.promotionsAmount.setText(PromotionsModel.promotionAmount);
        holder.promotionsCode.setText(PromotionsModel.promotionCode);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout notificatioLayout;
        private TextView promotionsDate, promotionsCode,promotionsAmount;

        private MyViewHolder(View view) {
            super(view);
            notificatioLayout = (LinearLayout) view.findViewById(R.id.notification_layout);
            promotionsDate = (TextView) view.findViewById(R.id.promotions_date);
            promotionsAmount = (TextView) view.findViewById(R.id.promotions_amount);
            promotionsCode = (TextView) view.findViewById(R.id.promotions_code);
//            notificatioLayout.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (v.getId() == notificatioLayout.getId()) {
//                context.startActivity(new Intent(context, HotelViewActivity.class));
                //Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        }

    }


}
