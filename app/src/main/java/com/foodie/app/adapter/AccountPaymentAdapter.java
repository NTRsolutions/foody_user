package com.foodie.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.foodie.app.R;
import com.foodie.app.activities.AccountPaymentActivity;
import com.foodie.app.activities.WelcomeScreenActivity;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.helper.SharedHelper;
import com.foodie.app.models.Card;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by santhosh@appoets.com on 30-08-2017.
 */

public class AccountPaymentAdapter extends BaseAdapter {

    private Context context_;
    private List<Card> list;
    private boolean isDeleteAvailable;

    public AccountPaymentAdapter(Context context, List<Card> list,boolean isDeleteAvailable) {
        this.context_ = context;
        this.list = list;
        this.isDeleteAvailable = isDeleteAvailable;
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
        final Card obj = list.get(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater mInflater = (LayoutInflater) context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.payment_method_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        if(isDeleteAvailable){
            holder.paymentLabel.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            holder.deleteTxt.setVisibility(View.VISIBLE);
        }
        else{
            holder.deleteTxt.setVisibility(View.GONE);
        }

        holder.paymentLabel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!isDeleteAvailable&&b){
                    AccountPaymentActivity.proceedToPayBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.deleteTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context_);
                builder.setMessage("Are you sure you want to delete?")
                        .setPositiveButton(context_.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                AccountPaymentActivity.deleteCard(obj.getId());

                            }
                        })
                        .setNegativeButton(context_.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(context_.getResources().getColor(R.color.theme));
                nbutton.setTypeface(nbutton.getTypeface(), Typeface.BOLD);
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(context_.getResources().getColor(R.color.theme));
                pbutton.setTypeface(pbutton.getTypeface(), Typeface.BOLD);

            }
        });

        holder.paymentLabel.setText("XXXX-XXXX-XXXX"+obj.getLastFour());
//        setIcon(holder.icon, obj.icon_id);
        holder.icon.setImageResource(R.drawable.ic_debit_card);
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
        RadioButton paymentLabel;
        @BindView(R.id.delete_txt)
        TextView deleteTxt;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
