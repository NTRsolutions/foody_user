package com.foodie.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.models.PaymentMethod;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by santhosh@appoets.com on 30-08-2017.
 */

public class AccountPaymentAdapter extends BaseAdapter {

    private Context context_;
    private List<PaymentMethod> list;

    public AccountPaymentAdapter(Context context, List<PaymentMethod> list) {
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
        PaymentMethod obj = list.get(position);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater mInflater = (LayoutInflater) context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.payment_method_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.paymentLabel.setText(obj.name);
        setIcon(holder.icon, obj.icon_id);

        return convertView;
    }

    private void setIcon(ImageView imgView, Integer id) {
        switch (id) {
            case 0:
                imgView.setImageResource(R.drawable.ic_debit_card);
                break;
            case 1:
                imgView.setImageResource(R.drawable.ic_cash);
                break;
            default:
                imgView.setImageResource(R.drawable.ic_cash);
                break;
        }
    }

    static class ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.payment_label)
        TextView paymentLabel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
