package com.foodie.app.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.fragments.OrderViewFragment;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.model.Checkout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentOrderDetailActivity extends AppCompatActivity {

    @BindView(R.id.order_id_txt)
    TextView orderIdTxt;
    @BindView(R.id.order_item_txt)
    TextView orderItemTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.order_placed_img)
    ImageView orderPlacedImg;
    @BindView(R.id.order_confirmed_img)
    ImageView orderConfirmedImg;
    @BindView(R.id.order_processed_img)
    ImageView orderProcessedImg;
    @BindView(R.id.order_pickedup_img)
    ImageView orderPickedupImg;
    @BindView(R.id.order_succeess_image)
    ImageView orderSucceessImage;
    @BindView(R.id.order_status_txt)
    TextView orderStatusTxt;
    @BindView(R.id.order_status_layout)
    RelativeLayout orderStatusLayout;
    @BindView(R.id.order_id_txt_2)
    TextView orderIdTxt2;
    @BindView(R.id.order_placed_time)
    TextView orderPlacedTime;

    Fragment orderFullViewFragment;
    FragmentManager fragmentManager;

    int priceAmount = 0;
    int discount = 0;
    int itemCount = 0;
    int itemQuantity = 0;
    String currency = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order_detail);
        ButterKnife.bind(this);


        //set Toolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0, 0);

        if (CommonClass.getInstance().checkoutData != null) {

            Checkout checkout = CommonClass.getInstance().checkoutData;
            //set Title values
//            orderIdTxt.setText("ORDER #000" + checkout.getId().toString());
//            if (checkout.getItems().size() != 0 && checkout.getItems() != null) {
//                itemCount = checkout.getItems().size();
//                for (int i = 0; i < itemCount; i++) {
//                    //Get Total item Quantity
//                    itemQuantity = itemQuantity + checkout.getItems().get(i).getQuantity();
//                    //Get product price
//                    if (checkout.getItems().get(i).getProduct().getPrices().getPrice() != null)
//                        priceAmount = priceAmount + (checkout.getItems().get(i).getQuantity() * checkout.getItems().get(i).getProduct().getPrices().getPrice());
//                    discount = discount + (checkout.getItems().get(i).getQuantity() * checkout.getItems().get(i).getProduct().getPrices().getDiscount());
//                }
//            }
            orderIdTxt.setText("ORDER #000" + checkout.getId().toString());
            itemQuantity = checkout.getInvoice().getQuantity();
            priceAmount = checkout.getInvoice().getNet();
            currency = checkout.getItems().get(0).getProduct().getPrices().getCurrency();
            if (itemQuantity == 1)
                orderItemTxt.setText(String.valueOf(itemQuantity) + " Item, " + currency + String.valueOf(priceAmount));
            else
                orderItemTxt.setText(String.valueOf(itemQuantity) + " Items, " + currency + String.valueOf(priceAmount));

            orderIdTxt2.setText("#000" + checkout.getId().toString());
            orderPlacedTime.setText(getTimeFromString(checkout.getCreatedAt()));
        }


        //set Fragment
        orderFullViewFragment = new OrderViewFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.order_detail_fargment, orderFullViewFragment).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CurrentOrderDetailActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }

    private String getTimeFromString(String time) {
        System.out.println("Time : " + time);
        String value = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

            if (time != null) {
                Date date = df.parse(time);
                value = sdf.format(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();

        }
        return value;
    }
}
